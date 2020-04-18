package com.curse2014.stormlightmod.objects.items;

import com.curse2014.stormlightmod.effects.StormlightEffect;
import com.curse2014.stormlightmod.init.BlockInitNew;
import com.curse2014.stormlightmod.init.EffectInit;
import com.curse2014.stormlightmod.init.ItemInitNew;
import com.curse2014.stormlightmod.util.helpers.KeyboardHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//import static com.curse2014.stormlightmod.init.EffectInit.STORMLIGHT_EFFECT;

public class SphereItem extends Item {
    private int maxStormlight = 4800;//should be some function of sphere type & size, when add diff types and sizes
    //game is 20 ticks/s so 4800 == 4 minutes;
    private int drainRate = 20;
    private float normalDrainRate = 0.001f;
    private String stormlight = "stormlight";
    private String inUse = "inUse";
    private int value;
    private int type;
    private int size;
    public EffectInstance effect = new EffectInstance(EffectInit.stormlight, this.maxStormlight);
    private Consumer<PlayerEntity> uh = new Consumer<PlayerEntity>() {
        @Override
        public void accept(PlayerEntity playerEntity) {

        }
    };
    private int count = 0;

    //diamond chip
    public SphereItem(Properties properties) {
        super(properties);
        this.type = 1;
        this.size = 1;
        this.value = 1;
        properties.maxStackSize(1);
        properties.maxDamage(this.maxStormlight);
        //properties.setNoRepair();
    }

    public SphereItem(Properties properties, int type, int size, int value) {
        super(properties);
        this.type = type;
        this.size = size;
        this.value = value;
        /* could do this but have to register other values anyway
        switch (type) {
            case 1:
                switch (size) {
                    case 1:
                        this.value = 1;
                        break;
                    case 2:
                        this.value = 5;
                    case 3:
                        this.value = 20;
                }
        }
        */
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        CompoundNBT compoundnbt = stack.getTag();
        if (compoundnbt == null) {
            stack.setTag(new CompoundNBT());
            compoundnbt = stack.getTag();
        }
        compoundnbt.putBoolean(this.inUse, false);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        CompoundNBT compoundnbt = itemStack.getTag();
        if (compoundnbt == null) {
            this.onCreated(itemStack, worldIn, playerIn);
            compoundnbt = itemStack.getTag();
        }
        if (compoundnbt.getBoolean(this.inUse)) {
            playerIn.removePotionEffect(effect.getPotion());
            compoundnbt.putBoolean(this.inUse, false);
        } else {
            EffectInstance temp = new EffectInstance(
                    EffectInit.stormlight, itemStack.getDamage() - 1
            );
            effect.combine(temp);
            playerIn.addPotionEffect(this.effect);
            compoundnbt.putBoolean(this.inUse, true);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        count += 1;
        if (count == 20) {
            count = 0;
            CompoundNBT compoundnbt = stack.getTag();
            if (compoundnbt == null) {
                this.onCreated(stack, worldIn, (PlayerEntity) entityIn);
                compoundnbt = stack.getTag();
            }
            int usingDrainRate = 20;
            if (stack.getDamage() <= usingDrainRate && compoundnbt.getBoolean(this.inUse)) {
                ((PlayerEntity) entityIn).removePotionEffect(this.effect.getPotion());
                compoundnbt.putBoolean(this.inUse, false);
            } else {
                if (compoundnbt.getBoolean(this.inUse)) {
                    stack.damageItem(usingDrainRate, (PlayerEntity) entityIn, uh);
                    //stack.setDamage(stack.getDamage() - usingDrainRate);
                } else {
                    stack.damageItem(1, (PlayerEntity) entityIn, uh);
                    //stack.setDamage(stack.getDamage() - 1);
                }
            }
            if (worldIn.isThundering()) {
                //move below line to world event
                worldIn.setTimeLightningFlash(1);
                worldIn.setThunderStrength(1.5f);
                worldIn.setRainStrength(1.5f);
                if (stack.getDamage() < stack.getMaxDamage() - 100) {
                    stack.setDamage(stack.getDamage() + 100);
                } else {
                    stack.setDamage(stack.getMaxDamage());
                }
            }
        }
    }
    /*
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getWorld().getBlockState(context.getPos()).getBlock() == BlockInitNew.DEF_BLOCK.get()) {
            for (ItemStack stack : context.getPlayer().inventory.mainInventory) {
                if (stack.isEmpty()) {
                    context.getPlayer().addItemStackToInventory(new ItemStack(ItemInitNew.DEF_ITEM.get()));
                    context.getItem().damageItem(1, context.getPlayer(), (playerIn) -> {
                        playerIn.sendBreakAnimation(context.getHand());
                    });
                    return ActionResultType.SUCCESS;
                }
            }
            context.getWorld().addEntity(new ItemEntity(context.getWorld(), context.getPos().getX(),
                    context.getPos().getY(), context.getPos().getZ(), new ItemStack(ItemInitNew.DEF_ITEM.get())));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }
    */
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getDamage() == 1) {
            tooltip.add(new StringTextComponent("This is a dun sphere, find a Highstorm to recharge it!"));
        }
        tooltip.add(new StringTextComponent(Integer.toString(this.value)));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        return itemStack.getDamage() > 1;
    }
}
