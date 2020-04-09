package net.curse2014.stormlightmod.init;

//import jdk.internal.jline.internal.Nullable;
import net.curse2014.stormlightmod.StormlightMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static Block myBlock;

    public static void registerAll(RegistryEvent.Register<Block> event) {
        if (!event.getName().equals(ForgeRegistries.BLOCKS.getRegistryName())) {
            return;
        }
        Block block = new Block(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(1.5f, 6f)
                .sound(SoundType.STONE)
        );
        myBlock = register("block_name", block);
    }

    private static <T extends Block> T register(String name, T block) {
        Item.Properties group = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
        return register(name, block, new BlockItem(block, group));
    }

    private static <T extends Block> T register(String name, T block, BlockItem item) {
        ResourceLocation id = StormlightMod.getId(name);
        block.setRegistryName(id);
        ForgeRegistries.BLOCKS.register(block);
        if (item != null) {
            ModItems.BLOCKS_TO_REGISTER.put(name,item);
        }
        return block;
    }
}
