package com.curse2014.stormlightmod.init;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.entities.ExampleEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES,
			StormlightMod.MOD_ID);

	public static final RegistryObject<EntityType<ExampleEntity>> EXAMPLE_ENTITY = ENTITY_TYPES
			.register("example_entity",
					() -> EntityType.Builder.<ExampleEntity>create(ExampleEntity::new, EntityClassification.CREATURE)
							.size(0.9f, 1.3f)
							.build(new ResourceLocation(StormlightMod.MOD_ID, "example_entity").toString()));
}
