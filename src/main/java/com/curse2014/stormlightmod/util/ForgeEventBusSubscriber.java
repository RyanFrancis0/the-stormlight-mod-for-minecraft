package com.curse2014.stormlightmod.util;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.init.DimensionInit;

import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Bus.FORGE)
public class ForgeEventBusSubscriber {

	@SubscribeEvent
	public static void registerDimensions(final RegisterDimensionsEvent event) {
		if (DimensionType.byName(StormlightMod.EXAMPLE_DIM_TYPE) == null) {
			DimensionManager.registerDimension(StormlightMod.EXAMPLE_DIM_TYPE, DimensionInit.EXAMPLE_DIM.get(), null,
					true);
		}
		//StormlightMod.LOGGER.info("Dimensions Registered!");
	}
}
