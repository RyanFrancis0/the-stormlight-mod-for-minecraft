package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldTickingEvent {
    @SubscribeEvent
    public static void onThunder(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote()) {
            return;
        }
        if (event.player.world.isThundering()) {
            event.player.world.setTimeLightningFlash(1);
            event.player.world.setThunderStrength(1.5f);
            event.player.world.setRainStrength(1f);
        }
    }
}
