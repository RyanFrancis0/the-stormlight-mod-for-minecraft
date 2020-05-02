package com.curse2014.stormlightmod;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import com.curse2014.stormlightmod.capabilities.PlayerInfoStorage;
import com.curse2014.stormlightmod.init.*;
import com.curse2014.stormlightmod.networking.StormlightModPacketHandler;
import com.curse2014.stormlightmod.world.gen.StormlightOreGen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Bus.MOD)//, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StormlightMod {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "stormlightmod";
	public static StormlightMod instance;
	// public static final WorldType EXAMPLE_WORLDTYPE = new ExampleWorldType();
	public static final ResourceLocation EXAMPLE_DIM_TYPE = new ResourceLocation(MOD_ID, "example");

	public StormlightMod() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
		modEventBus.addListener(this::setup);

		StormlightModPacketHandler.registerMessage();

		SoundInit.SOUNDS.register(modEventBus);
		ItemInitNew.ITEMS.register(modEventBus);
		BlockInitNew.BLOCKS.register(modEventBus);
		ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
		ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
		ModEntityTypes.ENTITY_TYPES.register(modEventBus);

		BiomeInit.BIOMES.register(modEventBus);
		DimensionInit.MOD_DIMENSIONS.register(modEventBus);
		//EffectInit.POTIONS.register(modEventBus);\

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

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(
					new ResourceLocation(StormlightMod.MOD_ID, "playerinfo"),
					new PlayerInfoProvider()
			);
		}
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(IPlayerInfo.class, new PlayerInfoStorage(), PlayerInfo::new);
	}

	/*@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(IPlayerInfo.class, new PlayerInfoStorage(), PlayerInfo::new);
	}
	*/
	private void setup(FMLCommonSetupEvent event) {
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
