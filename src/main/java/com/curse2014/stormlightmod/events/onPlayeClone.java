package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class onPlayeClone {
    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        PlayerEntity oldPLayer = event.getOriginal();
        PlayerEntity newPlayer = event.getPlayer();
        PlayerTickingEvent.flightPath = null;
        IPlayerInfo oldCapability = oldPLayer.getCapability(PlayerInfoProvider.PLAYER_INFO, null).orElse(null);
        IPlayerInfo newCapability = newPlayer.getCapability(PlayerInfoProvider.PLAYER_INFO, null).orElse(null);
        newCapability.setBlade(oldCapability.getBlade());
        while (newCapability.getIdeal() < oldCapability.getIdeal()) {
            newCapability.oathAccepted();
        }
        if (!event.isWasDeath()) {
            newCapability.changeStormlight(oldCapability.getStormlight());
        }
        ((PlayerInfo) newCapability).order = oldCapability.getOrder();

    }
}
