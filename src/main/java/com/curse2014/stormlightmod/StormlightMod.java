package com.curse2014.stormlightmod;

import com.curse2014.stormlightmod.world.gen.StormlightOreGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.curse2014.stormlightmod.init.BiomeInit;
import com.curse2014.stormlightmod.init.BlockInit;
import com.curse2014.stormlightmod.init.BlockInitNew;
import com.curse2014.stormlightmod.init.DimensionInit;
import com.curse2014.stormlightmod.init.ItemInitNew;
import com.curse2014.stormlightmod.init.ModContainerTypes;
import com.curse2014.stormlightmod.init.ModEntityTypes;
import com.curse2014.stormlightmod.init.ModTileEntityTypes;
import com.curse2014.stormlightmod.init.SoundInit;
import com.curse2014.stormlightmod.objects.blocks.ModCropBlock;
//import com.turtywurty.tutorialmod.world.worldtype.ExampleWorldType;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

@Mod("stormlightmod")
@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Bus.MOD)
public class StormlightMod {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "stormlightmod";
	public static StormlightMod instance;
	// public static final WorldType EXAMPLE_WORLDTYPE = new ExampleWorldType();
	public static final ResourceLocation EXAMPLE_DIM_TYPE = new ResourceLocation(MOD_ID, "example");

	public StormlightMod() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);

		SoundInit.SOUNDS.register(modEventBus);
		ItemInitNew.ITEMS.register(modEventBus);
		BlockInitNew.BLOCKS.register(modEventBus);
		ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
		ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
		ModEntityTypes.ENTITY_TYPES.register(modEventBus);

		BiomeInit.BIOMES.register(modEventBus);
		DimensionInit.MOD_DIMENSIONS.register(modEventBus);

		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		BlockInitNew.BLOCKS.getEntries().stream().filter(block -> !(block.get() instanceof ModCropBlock))
				.map(RegistryObject::get).forEach(block -> {
					final Item.Properties properties = new Item.Properties().group(StormlightItemGroup.instance);
					final BlockItem blockItem = new BlockItem(block, properties);
					blockItem.setRegistryName(block.getRegistryName());
					registry.register(blockItem);
				});

		LOGGER.debug("Registered BlockItems!");
	}

	@SubscribeEvent
	public static void onRegisterBiomes(final RegistryEvent.Register<Biome> event) {
		BiomeInit.registerBiomes();
	}

	private void setup(final FMLCommonSetupEvent event) {

	}

	@SubscribeEvent
	public static void onServerStarting(FMLServerStartingEvent event) {

	}

	@SubscribeEvent
	public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
		StormlightOreGen.generateOre();
	}

	public static class StormlightItemGroup extends ItemGroup {
		public static final ItemGroup instance = new StormlightItemGroup(ItemGroup.GROUPS.length, "stormlighttab");

		private StormlightItemGroup(int index, String label) {
			super(index, label);
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(BlockInit.example_block);
		}
	}
}
