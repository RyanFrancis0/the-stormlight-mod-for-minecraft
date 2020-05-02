package com.curse2014.stormlightmod.objects.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Timer;
import java.util.stream.Stream;

public class ShardBladeItem extends SwordItem {
    private boolean isHonourBlade = false;
    private boolean isDeadSpren = true;
    private PlayerEntity master = null;
    private String name = "Oathbringer";

    public ShardBladeItem(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
        /* still has a shadow
        this.addPropertyOverride(new ResourceLocation("invisible"), ((itemStack, world, livingEntity) -> {
            CompoundNBT compoundnbt = itemStack.getTag();
            if (compoundnbt == null) {
                //System.o
                itemStack.setTag(new CompoundNBT());
                compoundnbt = itemStack.getTag();
                compoundnbt.putString("player", "");
                compoundnbt.putString("name", this.name);
                compoundnbt.putFloat("invisible", 0.0F);
            }
            return 1.0F;// compoundnbt.getFloat("invisible");
        }));
         */
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        Material material = blockState.getMaterial();
        return material == Material.PLANTS ? 16.0f : 10.0f;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, net.minecraftforge.common.ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        return 0;
    }

    /*@Override
    public boolean canHarvestBlock(BlockState blockState) {
        return false;
    }
    */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        CompoundNBT compoundnbt = itemStack.getTag();
        String playerId = playerIn.getDisplayNameAndUUID().getString();
        if (compoundnbt == null) {
            itemStack.setTag(new CompoundNBT());
            compoundnbt = itemStack.getTag();
            compoundnbt.putString("player", "");
            compoundnbt.putString("name", this.name);
        }
        if (compoundnbt.getString("player").equals("")) {
            compoundnbt.putString("player", playerId);
            playerIn.sendMessage(new StringTextComponent(
                    "You have bonded " + compoundnbt.getString("name"))
            );
        } else {
            playerIn.sendMessage(new StringTextComponent(
                    "You cannot claim this blade.")
            );
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

}
