package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import com.curse2014.stormlightmod.networking.ShardbladePacket;
import com.curse2014.stormlightmod.networking.StormlightModPacketHandler;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityMetadataPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTickingEvent {
    private static int count = 0; //unneccesarry? however u spell that word. need to calibrate stormlight vals
    public static Vec3d flightPath = null;
    private static Vec3d flightVelocity = null;
    private static int duration = 150;
    private static EffectInstance a = new EffectInstance(Effects.SPEED, duration);
    private static EffectInstance b = new EffectInstance(Effects.STRENGTH, duration);
    private static EffectInstance c = new EffectInstance(Effects.WATER_BREATHING, duration);
    private static EffectInstance d = new EffectInstance(Effects.REGENERATION, duration);

    @SubscribeEvent
    public static void ingestedStormlight(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        IPlayerInfo playerInfo = event.player.getCapability(PlayerInfoProvider.PLAYER_INFO, null)
                .orElse(null);
        count++;
        if (count != 20) {
            return;
        }
        count = 0;
        playerInfo.changeStormlight(-1);
        if (playerInfo.getStormlight() == 0) {
            event.player.setGlowing(false);
            flightPath = null;
            event.player.removePotionEffect(Effects.STRENGTH);
            event.player.removePotionEffect(Effects.SPEED);
            event.player.removePotionEffect(Effects.WATER_BREATHING);
            event.player.removePotionEffect(Effects.REGENERATION);
            event.player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 180));
            event.player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 180));
            return;
        }
        if (playerInfo.getStormlight() > 0) {
            //no food decrease, resotre to half hunger if on 0
            event.player.setAir(event.player.getMaxAir());//why this no update? need packet?//it need server and client world
            event.player.heal(1.0F);
            event.player.fallDistance = 0.0f;
            //event.player.getFoodStats().setFoodSaturationLevel(event.player.getFoodStats().getSaturationLevel() + 1);
            event.player.addPotionEffect(new EffectInstance(Effects.SPEED, 180));//event.player.potion
            event.player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 180));
            //event.player.addPotionEffect(c);
            //event.player.addPotionEffect(d);
//                EntityDataManager entitydatamanager = event.player.getDataManager();
//                if (entitydatamanager.isDirty()) {
//                    StormlightModPacketHandler.INSTANCE.sendToServer(
//                            new SEntityMetadataPacket(event.player.getEntityId(), entitydatamanager, false)
//                    );
//                }
            //note to self shardplate should provide jumping bonus
            /*
             A person can only hold Stormlight for a few minutes at most, while a gemstone will hold it for several days
             -- the ones used as currency glow for about a week, while bigger gems last a while longer.[12][13][14]
             Perfect gemstones, like King's Drop, can hold it for centuries, if not forever.[15] When leaking from gems,
             Stormlight assumes their color.

             Human bodies are imperfect containers for the Stormlight, so the Stormlight leaves the Surgebinder's body
             over time, giving the Surgebinder a glowing effect with luminescent white vapor rising from the skin.
             Breathing out accelerates this leaving, but Stormlight substitutes for oxygen so the Surgebinder does not
             need to breathe.[6] The more Stormlight the Surgebinder holds, the faster it leaks out of the body.[19] If
             holding a small amount of Stormlight, Surgebinders are able to breathe in and suppress the glowing
             effect.[20] While doing this, they are able to talk without puffing out Stormlight.[21]

             In addition to being fuel for Surgebinding, Stormlight also increases the physical capabilities of the
             Surgebinder's body far beyond regular human levels. The Surgebinder gains superhuman strength, speed,
             endurance, stamina, and healing of both body and soul. They can survive falls hundreds of feet, and stand
             up right away, the damage immediately being healed by Stormlight. Almost any kind of injury can be healed,
             including bad eyesight, scar tissues, or Spiritual damage done by a Shardblade.[22] This healing is based
             upon the Cognitive identity of the Surgebinder, as such if a soldier sees the scars on his body, for
             example, as a part of his self, Stormlight does not heal those scars.[23]

             While inside the body, the Stormlight produces an intense adrenaline-like effect, urging the Surgebinder to
             action and motion, which may result in recklessness if the Surgebinder is not careful. It gives great
             energy, but when the Surgebinder runs out of Stormlight, they are left exhausted and feeling deflated
             similar to a pewter drag by a Pewterarm.[24]
             */
        }
        if (!event.player.world.isRemote()) {
            if (flightPath != null) {
                //event.player.setMotion(flightPath.x, flightPath.y, flightPath.z);
                event.player.fallDistance = 0.0F;
                if (flightVelocity == null) {
                    flightVelocity = flightPath;
                } else {
                    flightVelocity.add(flightPath);
                }
                //u can accelerate player A LOT so need to set ur own speed cap
                if (flightVelocity.length() < 1) {
                    event.player.addVelocity(flightPath.x, flightPath.y, flightPath.z);
                } else {
                    flightVelocity.subtract(flightPath);
                }
            } else if (flightVelocity != null) {
                event.player.fallDistance = 0.0F;
                flightVelocity = null;
            }
            if (playerInfo.getStormlight() > 0f) {
                //add white breathing effect (I hope)
                double d0 = (double) (16777215 >> 16 & 255) / 255.0D;
                double d1 = (double) (16777215 >> 8 & 255) / 255.0D;
                double d2 = (double) (16777215 >> 0 & 255) / 255.0D;

                event.player.world.addParticle(
                        ParticleTypes.ENTITY_EFFECT,
                        event.player.getPosXRandom(0.5D),
                        event.player.getPosYRandom(),
                        event.player.getPosZRandom(0.5D),
                        d0,
                        d1,
                        d2
                );
                if (!event.player.isGlowing()) {
                    event.player.setGlowing(true);
                }
            }
        }
        System.out.println(playerInfo.getStormlight());
    }
}
