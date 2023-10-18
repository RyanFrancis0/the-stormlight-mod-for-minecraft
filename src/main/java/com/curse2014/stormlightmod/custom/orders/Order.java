package com.curse2014.stormlightmod.custom.orders;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;

public abstract class Order {
    protected boolean isVPressed = false;
    protected boolean isXPressed = false;
    protected boolean isVActive = false;
    protected boolean isXActive = false;

    public abstract String getName();

    public void vPressed() {
        this.isVPressed = true;
    }

    public void xPressed() {
        this.isXPressed = true;
    }

    public void rclk(PlayerEntity player) {}

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
    private static final double d0 = (double) (16777215 >> 16 & 255) / 255.0D;
    private static final double d1 = (double) (16777215 >> 8 & 255) / 255.0D;
    private static final double d2 = (double) (16777215 >> 0 & 255) / 255.0D;
    private static final int maxParticles = 15;
    private static final int potionEffectsDuration = 180; // I think 180 = 8 sec?
    private static final int minHoldBreathThreshold = 100;
    private static final int normalDrainRate = -1;
    private static final int reducedDrainRate = normalDrainRate + 1;

    protected EffectInstance getEffectInstance(Effect effect) {
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

    public void on_tick(PlayerEntity player) {
        IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
        //Try finding some kind of player put item in container event first
//        if (!player.inventory.hasItemStack(playerInfo.getCurrentBlade())) {
//
//        }
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
    }
}

