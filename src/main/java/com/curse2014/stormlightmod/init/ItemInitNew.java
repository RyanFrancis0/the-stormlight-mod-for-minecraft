package com.curse2014.stormlightmod.init;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.objects.items.SpecialItem;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInitNew {
	
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, StormlightMod.MOD_ID);
	
	public static final RegistryObject<Item> DEF_ITEM = ITEMS.register("def_item", () -> new SpecialItem(new Item.Properties().group(StormlightMod.StormlightItemGroup.instance)));
}
