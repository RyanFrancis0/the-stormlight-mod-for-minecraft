package com.curse2014.stormlightmod.init;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.world.dimension.ExampleModDimension;

import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DimensionInit {
	
	public static final DeferredRegister<ModDimension> MOD_DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, StormlightMod.MOD_ID);
	
	public static final RegistryObject<ModDimension> EXAMPLE_DIM = MOD_DIMENSIONS.register("example_dim", () -> new ExampleModDimension());
}
