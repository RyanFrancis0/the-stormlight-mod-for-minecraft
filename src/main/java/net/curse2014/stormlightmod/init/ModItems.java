package net.curse2014.stormlightmod.init;
import net.curse2014.stormlightmod.StormlightMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    static final Map<String, BlockItem> BLOCKS_TO_REGISTER = new LinkedHashMap<>();
    public static Item myItem;

    public static void registerAll(RegistryEvent.Register<Item> event) {
        if (!event.getName().equals(ForgeRegistries.BLOCKS.getRegistryName())) {
            return;
        }
        //Blocks
        BLOCKS_TO_REGISTER.forEach(ModItems::register);
        //Items



        //myItem = register();
    }
    private static <T extends Item> T register(String name, T item) {
        ResourceLocation id = StormlightMod.getId(name);
        item.setRegistryName(id);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }


    ModItems() {
    }
}
