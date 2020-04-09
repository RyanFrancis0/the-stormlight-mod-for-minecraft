package com.curse2014.stormlightmod.init;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.tileentity.ExampleChestTileEntity;
import com.curse2014.stormlightmod.tileentity.QuarryTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(
			ForgeRegistries.TILE_ENTITIES, StormlightMod.MOD_ID);

	public static final RegistryObject<TileEntityType<QuarryTileEntity>> QUARRY = TILE_ENTITY_TYPES.register("quarry",
			() -> TileEntityType.Builder.create(QuarryTileEntity::new, BlockInit.quarry).build(null));

	public static final RegistryObject<TileEntityType<ExampleChestTileEntity>> EXAMPLE_CHEST = TILE_ENTITY_TYPES
			.register("example_chest", () -> TileEntityType.Builder
					.create(ExampleChestTileEntity::new, BlockInitNew.EXAMPLE_CHEST.get()).build(null));
}
