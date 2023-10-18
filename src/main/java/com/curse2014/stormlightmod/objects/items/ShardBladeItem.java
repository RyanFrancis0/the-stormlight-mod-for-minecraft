package com.curse2014.stormlightmod.objects.items;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.init.ItemInit;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.stream.Stream;

public class ShardBladeItem extends SwordItem {
    public enum Type {
        LIVING,
        DEAD,
        HONOURBLADE
    }

    public ShardBladeItem(Properties properties) {
        super(ItemTier.DIAMOND,20,6, properties);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack a, World world, BlockState b, BlockPos bPos, LivingEntity c) {
        return true;
    }

    /**
     * Not a pickaxe.
     * @param b
     * @return false
     */
    @Override
    public boolean canHarvestBlock(BlockState b) {
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        Material material = blockState.getMaterial();
        return material == Material.PLANTS ? 16.0f : 10.0f;
    }

    /**
     * Shardblades don't wear and tear.
     * @return
     */
    @Override
    public boolean isDamageable() {
        return false;
    }

    /**
     * Shardblades have diff modifiers to normal swords. Such as extended reach.
     * @param equipmentSlotType
     * @return
     */
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlotType) {
        //PlayerEntity.REACH_DISTANCE says (in this update) standard player reach is 5.0D.
        double shardbladeAdditionalReach = 3.0D;
        Multimap<String, AttributeModifier> attributeModifiers = super.getAttributeModifiers(equipmentSlotType);
        if (equipmentSlotType == EquipmentSlotType.MAINHAND) {
            attributeModifiers.put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA2"), "Weapon modifier", shardbladeAdditionalReach, AttributeModifier.Operation.ADDITION));
        }
        return attributeModifiers;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(playerIn);
        if (playerInfo.isMyBlade(itemStack)) {
            return ActionResult.resultPass(itemStack);
        }
        return ActionResult.resultConsume(playerIn.getHeldItem(handIn));
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
            String action;
            if (playerInfo.isBondedTo(stack)) {
                playerInfo.unbondBlade(stack);
                action = "You have unbonded ";
                if (stack.getItem().equals(ItemInit.windrunner_honourblade)) {//|| ea of honourblades
                    //set player order to spren order
                }
            } else {
                playerInfo.bondBlade(stack, false);
                action = "You have bonded ";
                if (stack.getItem().equals(ItemInit.windrunner_honourblade)) {
                    //set player order
                }
            }
            player.sendMessage(new StringTextComponent(action + stack.getDisplayName()));
        }
        return stack;
    }
}
