package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTickingEvent {
    private static int count = 0; //unneccesarry? however u spell that word. need to calibrate stormlight vals

    @SubscribeEvent
    public static void ingestedStormlight(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !event.player.world.isRemote()) {
            return;
        }
        IPlayerInfo playerInfo = event.player.getCapability(PlayerInfoProvider.PLAYER_INFO, null)
                .orElse(null);
        if (playerInfo.getStormlight() > 0f) {
            count++;
            if (count != 20) {
                return;
            }
            count = 0;
            playerInfo.setStormlight(playerInfo.getStormlight() - 1);
            event.player.setGlowing(true);
            //add white breathing effect (I hope)
            double d0 = (double) (2039178 >> 16 & 255) / 255.0D;
            double d1 = (double) (2039178 >> 8 & 255) / 255.0D;
            double d2 = (double) (2039178 >> 0 & 255) / 255.0D;

            event.player.world.addParticle(
                    ParticleTypes.ENTITY_EFFECT,
                    event.player.getPosXRandom(0.5D),
                    event.player.getPosYRandom(),
                    event.player.getPosZRandom(0.5D),
                    d0,
                    d1,
                    d2
            );
        } else {
            playerInfo.setStormlight(0f);
            event.player.setGlowing(false);
        }
    }
}
