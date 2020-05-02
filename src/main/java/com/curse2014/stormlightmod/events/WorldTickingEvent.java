package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldTickingEvent {
    @SubscribeEvent
    public static void onThunder(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote() || !event.world.isThundering() || event.phase != TickEvent.Phase.START) {
            return;
        }
        System.out.println("is indeed thundering and ticking");
        event.world.setTimeLightningFlash(1);
        event.world.setThunderStrength(1.5f);
        event.world.setRainStrength(5f);
    }
}
