package com.curse2014.stormlightmod.capabilities;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.networking.PlayerCapabilityPacket;
import com.curse2014.stormlightmod.networking.StormlightModPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityHandler {

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(
                    new ResourceLocation(StormlightMod.MOD_ID, "playerinfo"),
                    new PlayerInfoProvider()
            );
        }
    }

    @SubscribeEvent
    public static void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
        sendClientCapabilityInfo(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerSpawn(EntityJoinWorldEvent event) {
        Entity eventEntity = event.getEntity();
        if (eventEntity instanceof PlayerEntity) {
            sendClientCapabilityInfo((PlayerEntity) eventEntity);
        }
    }

    /**
     * Not related to servers/clients (sides). All capability data wiped on death or return from end.
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            //Copy old player data to new player
            IPlayerInfo oldCapability = PlayerInfo.getFromPlayer(event.getOriginal());
            IPlayerInfo newCapability = PlayerInfo.getFromPlayer(event.getPlayer());
            newCapability.setValuesFromNBT(oldCapability.convertToNBT());
            //Now Change data as appropriate for dying
            newCapability.setStormlight(0);
        }
    }

    /**
     * Capability data is stored on server side. This means two things:
     *  1. When updating capcability info around the way DO NOT differentiate between sides.
     *          This will cause each side to get out of sync.
     *  2. On events that inevitably send a server player out of whack with client player (such as logging in),
     *          client capability will need to be updated to put both in sync.
     */
    private static void sendClientCapabilityInfo(PlayerEntity player) {
        if (!player.world.isRemote) { //Confirm we are in fact server sending to client
            ServerPlayerEntity p = (ServerPlayerEntity) player;
            CompoundNBT nbt = (CompoundNBT) PlayerInfo.getFromPlayer(p).convertToNBT();
            StormlightModPacketHandler.INSTANCE.sendTo(new PlayerCapabilityPacket(p, nbt), p.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IPlayerInfo.class, new PlayerInfoStorage(), PlayerInfo::new);
    }
}
