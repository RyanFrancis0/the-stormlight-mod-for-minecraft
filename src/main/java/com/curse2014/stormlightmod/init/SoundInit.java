package com.curse2014.stormlightmod.init;

import com.curse2014.stormlightmod.StormlightMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundInit {

	public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS,
			StormlightMod.MOD_ID);

	public static final RegistryObject<SoundEvent> AMBIENT = SOUNDS.register("entity.example_entity.ambient",
			() -> new SoundEvent(new ResourceLocation(StormlightMod.MOD_ID, "entity.example_entity.ambient")));
}
