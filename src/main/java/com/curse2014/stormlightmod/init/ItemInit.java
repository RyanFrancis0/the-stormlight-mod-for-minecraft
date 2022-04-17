package com.curse2014.stormlightmod.init;

import java.util.function.Supplier;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.objects.items.*;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Bus.MOD)
@ObjectHolder(StormlightMod.MOD_ID)
public class ItemInit {
	public static final Item example_item = null;
	public static final Item test_item = null;
	public static final Item special_item = null;

	public static final Item shardblade_item = new ShardBladeItem(
			ItemTier.DIAMOND,
			20,
			6,
			new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic)
	);

	// Tools
	public static final Item example_sword = null;
	public static final Item example_pickaxe = null;
	public static final Item example_shovel = null;
	public static final Item example_axe = null;
	public static final Item example_hoe = null;

	// Armor
	public static final Item test_helmet = null;
	public static final Item test_chestplate = null;
	public static final Item test_leggings = null;
	public static final Item test_boots = null;

	/**
	 * remember each item needs its own instance of item properties
	 * @param event
	 */
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> reg = event.getRegistry();
		reg.register(
				new Item(new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic)
				).setRegistryName("example_item"));
		reg.register(
				new Item(new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic)
						.food(new Food.Builder().hunger(6).saturation(1.2f)
								.effect(new EffectInstance(Effects.ABSORPTION, 6000, 5), 0.7f).build())
						).setRegistryName("test_item")
		);
		reg.register(
				new SpecialItem(new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic))
				.setRegistryName("special_item")
		);

		reg.register(new SphereItem(
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic),
				SphereItem.POLESTONE.EMERALD,
				SphereItem.DENOMINATION.BROAM
		).setRegistryName("polestones/emerald_broam"));
		reg.register(new SphereItem(
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic),
				SphereItem.POLESTONE.EMERALD,
				SphereItem.DENOMINATION.MARK
		).setRegistryName("polestones/emerald_mark"));
		reg.register(new SphereItem(
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic),
				SphereItem.POLESTONE.EMERALD,
				SphereItem.DENOMINATION.CHIP
		).setRegistryName("polestones/emerald_chip"));
		reg.register(new SphereItem(
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic),
				SphereItem.POLESTONE.DIAMOND,
				SphereItem.DENOMINATION.BROAM
		).setRegistryName("polestones/diamond_broam"));
		reg.register(new SphereItem(
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic),
				SphereItem.POLESTONE.DIAMOND,
				SphereItem.DENOMINATION.MARK
		).setRegistryName("polestones/diamond_mark"));
		reg.register(new SphereItem(
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic),
				SphereItem.POLESTONE.DIAMOND,
				SphereItem.DENOMINATION.CHIP
		).setRegistryName("polestones/diamond_chip"));


		reg.register(shardblade_item.setRegistryName("shard_blade_item"));

		// Tools
		reg.register(
				new SwordItem(ModItemTier.EXAMPLE, 7, 5.0f, new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic))
						.setRegistryName("example_sword"));
		reg.register(
				new PickaxeItem(ModItemTier.EXAMPLE, 4, 5.0f, new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic))
						.setRegistryName("example_pickaxe"));
		reg.register(
				new ShovelItem(ModItemTier.EXAMPLE, 2, 5.0f, new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic))
						.setRegistryName("example_shovel"));
		reg.register(
				new AxeItem(ModItemTier.EXAMPLE, 11, 3.0f, new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic))
						.setRegistryName("example_axe"));
		reg.register(
				new HoeItem(ModItemTier.EXAMPLE, 5.0f, new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic))
						.setRegistryName("example_hoe"));

		// Armor
		reg.register(new ArmorItem(ModArmorMaterial.TEST, EquipmentSlotType.HEAD,
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic)).setRegistryName("test_helmet"));
		reg.register(new ArmorItem(ModArmorMaterial.TEST, EquipmentSlotType.CHEST,
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic)).setRegistryName("test_chestplate"));
		reg.register(new ArmorItem(ModArmorMaterial.TEST, EquipmentSlotType.LEGS,
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic)).setRegistryName("test_leggings"));
		reg.register(new ArmorItem(ModArmorMaterial.TEST, EquipmentSlotType.FEET,
				new Item.Properties().group(StormlightMod.StormlightItemGroup.Basic)).setRegistryName("test_boots"));
	}

	public enum ModItemTier implements IItemTier {
		// int harvestLevel, int maxUses, float efficiency, float attackDamage, int
		// enchantability, Supplier<Ingredient> repairMaterial
		EXAMPLE(4, 1500, 15.0F, 7.0F, 250, () -> {
			return Ingredient.fromItems(ItemInit.example_item);
		});

		private final int harvestLevel;
		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int enchantability;
		private final LazyValue<Ingredient> repairMaterial;

		ModItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability,
				Supplier<Ingredient> repairMaterial) {
			this.harvestLevel = harvestLevel;
			this.maxUses = maxUses;
			this.efficiency = efficiency;
			this.attackDamage = attackDamage;
			this.enchantability = enchantability;
			this.repairMaterial = new LazyValue<>(repairMaterial);
		}

		@Override
		public int getMaxUses() {
			return this.maxUses;
		}

		@Override
		public float getEfficiency() {
			return this.efficiency;
		}

		@Override
		public float getAttackDamage() {
			return this.attackDamage;
		}

		@Override
		public int getHarvestLevel() {
			return this.harvestLevel;
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return this.repairMaterial.getValue();
		}
	}

	public enum ModArmorMaterial implements IArmorMaterial {
		TEST(StormlightMod.MOD_ID + ":test", 5, new int[] { 7, 9, 11, 7 }, 420, SoundEvents.field_226124_Y_, 6.9F, () -> {
			return Ingredient.fromItems(ItemInit.test_item);
		});

		private static final int[] MAX_DAMAGE_ARRAY = new int[] { 16, 16, 16, 16 };
		private final String name;
		private final int maxDamageFactor;
		private final int[] damageReductionAmountArray;
		private final int enchantability;
		private final SoundEvent soundEvent;
		private final float toughness;
		private final LazyValue<Ingredient> repairMaterial;

		ModArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountIn,
				int enchantabilityIn, SoundEvent soundEventIn, float toughnessIn,
				Supplier<Ingredient> repairMaterialIn) {
			this.name = nameIn;
			this.maxDamageFactor = maxDamageFactorIn;
			this.damageReductionAmountArray = damageReductionAmountIn;
			this.enchantability = enchantabilityIn;
			this.soundEvent = soundEventIn;
			this.toughness = toughnessIn;
			this.repairMaterial = new LazyValue<>(repairMaterialIn);
		}

		@Override
		public int getDurability(EquipmentSlotType slotIn) {
			return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return this.damageReductionAmountArray[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return this.soundEvent;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return this.repairMaterial.getValue();
		}

		@OnlyIn(Dist.CLIENT)
		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public float getToughness() {
			return this.toughness;
		}
	}
}
