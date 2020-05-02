package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import com.curse2014.stormlightmod.objects.items.SphereItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OnDropEvent {
    private boolean keepTrach = false;
    @SubscribeEvent
    public static void onDropEvent(ItemTossEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack tossed = event.getEntityItem().getItem();
        if (tossed.getItem() instanceof SphereItem) {
            if (tossed.getTag().getBoolean("inUse")) {
                System.out.println("dropped an active sphere");
                tossed.getTag().putBoolean("inUse", false);
                player.removePotionEffect(((SphereItem) tossed.getItem()).effect.getPotion());
            }
        }
        if (tossed.getItem() instanceof ShardBladeItem) {
            //dismiss item. consider something to let people share blades in the future.
            //look into custom dismiss key perhaps and event.getEntityItem().onGround?
            System.out.println(PlayerInfoProvider.PLAYER_INFO);
            //LazyOptional<IPlayerInfo> blade = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null);
            //IPlayerInfo um = null;
            IPlayerInfo what = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null).orElse(null);
            event.getEntityItem().remove();
            /*
            what.setBlade(event.getEntityItem().getEntityId());
            System.out.println(event.getEntityItem().getEntityId());
            IPlayerInfo uhm = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null).orElse(null);
            System.out.println(uhm.getBlade());

            //player.getEntityWorld().getChunkAt(player.getPosition()).removeEntity(event.getEntityItem());
            //event.getEntityItem().remove(true);
            //event.getEntityItem().getRenderBoundingBox().shrink(10);
            event.getEntityItem().setNoDespawn();
            //event.getEntityItem().setInvisible(true);
            //event.getEntityItem().setInfinitePickupDelay();
            event.getEntityItem().changeDimension(DimensionType.THE_END);
            System.out.println(event.getPlayer().getEntityWorld().getServer().getWorld(DimensionType.THE_END).getEntities()
                   .collect(Collectors.toList()));
            //event.getEntityItem().revive();
             */
        }
    }
}
