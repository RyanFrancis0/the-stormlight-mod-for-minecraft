package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldTickingEvent {
    @SubscribeEvent
    public static void onThunder(TickEvent.WorldTickEvent event) {
        WorldInfo world = event.world.getWorldInfo();
        if (!world.isThundering() || event.phase != TickEvent.Phase.START) {
            return;
        }
        //System.out.println(event.world.thunderingStrength);
        world.setThunderTime(100);//.setTimeLightningFlash(1);
        world.setRainTime(100);
        //world.setThunderStrength(1.5f);
        //event.world.setRainStrength(3f);
    }
}
