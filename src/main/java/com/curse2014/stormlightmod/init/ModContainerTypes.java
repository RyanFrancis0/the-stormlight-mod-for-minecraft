package com.curse2014.stormlightmod.init;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.container.ExampleChestContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(
			ForgeRegistries.CONTAINERS, StormlightMod.MOD_ID);

	public static final RegistryObject<ContainerType<ExampleChestContainer>> EXAMPLE_CHEST = CONTAINER_TYPES
			.register("example_chest", () -> IForgeContainerType.create(ExampleChestContainer::new));
}
