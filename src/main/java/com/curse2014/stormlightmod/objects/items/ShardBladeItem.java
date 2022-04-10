package com.curse2014.stormlightmod.objects.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
    }

    @Override
    public boolean onBlockDestroyed(ItemStack a, World world, BlockState b, BlockPos bPos, LivingEntity c) {
        world.removeBlock(bPos, false);
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        Material material = blockState.getMaterial();
        return material == Material.PLANTS ? 16.0f : 10.0f;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity entity, LivingEntity player) {
        if (entity.getTotalArmorValue() < 4) {
            entity.setHealth(0);
        }
        return true;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        CompoundNBT compoundnbt = itemStack.getTag();
        String playerId = playerIn.getDisplayNameAndUUID().getString();
        if (compoundnbt == null) {
            itemStack.setTag(new CompoundNBT());
            compoundnbt = itemStack.getTag();
            compoundnbt.putString("player", playerIn.getName().getString());
            compoundnbt.putString("name", this.name);
        }
        if (compoundnbt.getString("player").equals("Dev")) {
            compoundnbt.putString("player", playerId);
            playerIn.sendMessage(new StringTextComponent(
                    "You have bonded " + compoundnbt.getString("name"))
            );
        } else if (!compoundnbt.getString("player").equals(playerIn.getName().getString())) {
            playerIn.sendMessage(new StringTextComponent(
                    "You cannot claim this blade.")
            );
        } else {
            playerIn.sendMessage(new StringTextComponent(
                    "Your name is " + playerIn.getName().getString() + playerIn.getDisplayName()));
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

}
