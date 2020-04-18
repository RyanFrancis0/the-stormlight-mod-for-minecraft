package com.curse2014.stormlightmod.init;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.effects.StormlightEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
//@ObjectHolder(StormlightMod.MOD_ID)
public class EffectInit {
    /*
    public static final DeferredRegister<Effect> POTIONS = new DeferredRegister<>(ForgeRegistries.POTIONS, StormlightMod.MOD_ID);
    public static final RegistryObject<Effect> STORMLIGHT_EFFECT = POTIONS.register("stormlight_effect", () -> new StormlightEffect(EffectType.BENEFICIAL, 1));
    */
    public static final Effect stormlight = new StormlightEffect().setRegistryName(StormlightMod.MOD_ID, "stormlight_effect");

    @SubscribeEvent
    public static void registerEffect(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(stormlight);
    }

}
