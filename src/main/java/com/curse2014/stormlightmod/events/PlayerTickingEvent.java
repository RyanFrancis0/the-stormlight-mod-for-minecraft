package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.objects.items.SphereItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTickingEvent {
    //DO NOT WRITE TO STATIC FIELDS TO AFFECT TICK EVENT. IT MESSES UP LOGICAL CLIENT SERVER SYNC. At least I'm a self taught expert on those now I suppose.
    //I reckon way to go is not to try to slow down ticks, but to get used to them as a measure of speed. eveything is in the 1000s now.minutes are 10000s now
    //1 tick treying to be 0.05 seconds (20 ticks/s). 1 minecraft day == 24000 ticks = 20 minutes.1200 ticks == 1 minute. 1 minecraft week == 168,000 ticks.
    //players need to drain faster than spheres do. Which means spheres need to charge players faster than that.
    // gonna take some calibrating for all the diff sphere types
    // so what should middle of the range sphere do. or the median anyway.
    //ah I could divorce the idea of time from quantity
    //shere could normal drain or charge once every 20 (or yay) ticks has passed, so long as it stores tick.
    //dont think i want player to do that, so build off of that
    //player could drain quickly and suck in quickly. but high volume might make stormlight bar kind of weird
    public static Vec3d flightPath = null;
    private static Vec3d flightVelocity = null;
    private static double d0 = (double) (16777215 >> 16 & 255) / 255.0D;
    private static double d1 = (double) (16777215 >> 8 & 255) / 255.0D;
    private static double d2 = (double) (16777215 >> 0 & 255) / 255.0D;
    private static int maxParticles = 15;
    private static int potionEffectsDuration = 180; // I think 180 = 8 sec?
    private static int minHoldBreathThreshold = 100;
    private static int normalDrainRate = -1;
    private static int reducedDrainRate = normalDrainRate + 1;

    private static EffectInstance getEffectInstance(Effect effect) {
        return new EffectInstance(effect, potionEffectsDuration, 0, false, false);
    }

    public static boolean isHoldingBreath(PlayerEntity p, float s) {
        return s < minHoldBreathThreshold || p.areEyesInFluid(FluidTags.WATER);
    }

    private static void addBreathingOutParticles(PlayerEntity player, float stormlight) {
        int nParticles = 1; //rand.nextInt(maxParticles);
//        if (isHoldingBreath(player, stormlight)) {
//            nParticles = 1; //player breathes out less stormlight when holding their breath.
//        }
        for (int i = 0; i < nParticles; i++) {
            player.world.addParticle(
                    ParticleTypes.ENTITY_EFFECT,
                    player.getPosXRandom(0.5D),
                    player.getPosYRandom(),
                    player.getPosZRandom(0.5D),
                    d0,
                    d1,
                    d2
            );
        }
    }

    @SubscribeEvent
    public static void ingestedStormlight(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        PlayerEntity player = event.player;
        IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
        float stormlight = playerInfo.getStormlight();
        float newStormlight = stormlight + (isHoldingBreath(player, stormlight) ? normalDrainRate / 2f : normalDrainRate) + (player.world.isThundering() ? PlayerInfo.CHARGE_RATE : 0);
        if (stormlight > 0) {
            playerInfo.setStormlight(newStormlight);
            stormlight = newStormlight;
            if (stormlight % 20 != 0) { // 20 == 1 second soo just slow down how fast this is happenning
                return;
            }
            player.setGlowing(stormlight >= minHoldBreathThreshold);
            if (stormlight <= 0) {
                flightPath = null;
                player.removePotionEffect(Effects.SATURATION);
                player.removePotionEffect(Effects.SPEED);
                player.removePotionEffect(Effects.WATER_BREATHING);
                player.removePotionEffect(Effects.REGENERATION);
                player.addPotionEffect(getEffectInstance(Effects.MINING_FATIGUE));
                player.addPotionEffect(getEffectInstance(Effects.SLOWNESS));
                return;
            }
            addBreathingOutParticles(player, stormlight);
            player.fallDistance = 0.0F;
            player.addPotionEffect(getEffectInstance(Effects.SATURATION));
            player.addPotionEffect(getEffectInstance(Effects.REGENERATION));
            player.addPotionEffect(getEffectInstance(Effects.SPEED));
            if (player.areEyesInFluid(FluidTags.WATER)) {
                player.addPotionEffect(getEffectInstance(Effects.WATER_BREATHING));
            } else {
                if (player.getActivePotionEffect(Effects.WATER_BREATHING) != null) {
                    player.removePotionEffect(Effects.WATER_BREATHING);
                }
            }
            //player.notifyDataManagerChange(player.getDataManager().getEntry());
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
        } else {
            EffectInstance why = player.getActivePotionEffect(Effects.MINING_FATIGUE);
            EffectInstance why2 = player.getActivePotionEffect(Effects.SLOWNESS);
            if (why != null && why.getDuration() == 0) {
                player.removePotionEffect(Effects.MINING_FATIGUE);
            }
            if (why2 != null && why2.getDuration() == 0) {
                player.removePotionEffect(Effects.SLOWNESS);
            }
        }
        //Handle order's surges and passive effects
        if (!player.world.isRemote()) {
            if (flightPath != null) {
                //player.setMotion(flightPath.x, flightPath.y, flightPath.z);
                player.fallDistance = 0.0F;
                if (flightVelocity == null) {
                    flightVelocity = flightPath;
                } else {
                    flightVelocity.add(flightPath);
                }
                //u can accelerate player A LOT so need to set ur own speed cap
                if (flightVelocity.length() < 1) {
                    player.addVelocity(flightPath.x, flightPath.y, flightPath.z);
                } else {
                    flightVelocity.subtract(flightPath);
                }
            } else if (flightVelocity != null) {
                player.fallDistance = 0.0F;
                flightVelocity = null;
            }
        }
    }
}
