package com.curse2014.stormlightmod.objects.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
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
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        Material material = blockState.getMaterial();
        return material == Material.PLANTS ? 16.0f : 10.0f;
    }

    @Override


    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (this.master == null) {
            this.master = playerIn;
            playerIn.sendMessage(new StringTextComponent("You have bonded " + name));

        } else {
            if (!master.inventory.mainInventory.contains(this)) {
            }
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

}
