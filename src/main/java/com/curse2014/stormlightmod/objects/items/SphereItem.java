package com.curse2014.stormlightmod.objects.items;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import com.curse2014.stormlightmod.effects.StormlightEffect;
import com.curse2014.stormlightmod.init.BlockInitNew;
import com.curse2014.stormlightmod.init.EffectInit;
import com.curse2014.stormlightmod.init.ItemInitNew;
import com.curse2014.stormlightmod.util.helpers.KeyboardHelper;
import mcp.MethodsReturnNonnullByDefault;
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

public class SphereItem extends SwordItem {
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
            playerEntity.sendBreakAnimation(playerEntity.getActiveHand());
        }
    };
    private int count = 0;

    //diamond chip
    public SphereItem(Properties properties) {
        super(ItemTier.WOOD, 0, 0, properties.maxDamage(4800).defaultMaxDamage(4800));
        this.type = 1;
        this.size = 1;
        this.value = 1;
        //properties.maxStackSize(1);
    }

    /*public SphereItem(Properties properties, int type, int size, int value) {
        super(properties);
        this.type = type;
        this.size = size;
        this.value = value;
        //could do this but have to register other values anyway
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

    }*/

    /*
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (!world.isRemote) {
            if (player != null) {
                context.getItem().damageItem(1, player, (p_220043_1_) -> {
                    p_220043_1_.sendBreakAnimation(context.getHand());
                });
            }
        }
        return ActionResultType.SUCCESS;
    }
     */

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
        } else if (itemStack.getDamage() > 1) {
            /*
            EffectInstance temp = new EffectInstance(
                    EffectInit.stormlight,
                    getStormlight(itemStack)
            );
            effect.combine(temp);
            playerIn.addPotionEffect(this.effect);
             */
            compoundnbt.putBoolean(this.inUse, true);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        count += 1;
        if (count == 20) {
            PlayerEntity player = ((PlayerEntity) entityIn);
            IPlayerInfo playerInfo = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null)
                    .orElse(null);
            count = 0;
            CompoundNBT compoundnbt = stack.getTag();
            if (compoundnbt == null) {
                System.out.println("huh");
                this.onCreated(stack, worldIn, (PlayerEntity) entityIn);
                compoundnbt = stack.getTag();
            }
            int usingDrainRate = 20;
            //System.out.println(stack.getMaxDamage());
            if (this.getStormlight(stack) <= usingDrainRate) {
                if (compoundnbt.getBoolean(this.inUse)) {
                    player.removePotionEffect(this.effect.getPotion());
                    compoundnbt.putBoolean(this.inUse, false);
                }
                stack.setDamage(stack.getMaxDamage() - 1);
            } else {
                if (compoundnbt.getBoolean(this.inUse)) {
                    //stack.damageItem(usingDrainRate, player, (p_220000_1_) ->
                    //        p_220000_1_.sendBreakAnimation(Hand.OFF_HAND));
                    stack.setDamage(stack.getDamage() + usingDrainRate);
                    playerInfo.setStormlight(playerInfo.getStormlight() + usingDrainRate);
                    System.out.println(playerInfo.getStormlight());
                } else {
                    //stack.damageItem(1, player, (p_220000_1_) ->
                    //        p_220000_1_.sendBreakAnimation(Hand.OFF_HAND));
                    stack.setDamage(stack.getDamage() + 1);
                }
            }
            if (worldIn.isThundering()) {
                if (100 < stack.getDamage()) {
                    stack.setDamage(stack.getDamage() - 100);
                } else {
                    stack.setDamage(0);
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
        if (getStormlight(stack) == 1) {
            tooltip.add(new StringTextComponent("This is a dun sphere, find a Highstorm to recharge it!"));
        }
        tooltip.add(new StringTextComponent(Integer.toString(this.value)));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
        return false;
    }

    private int getStormlight(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamage();
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        return getStormlight(itemStack) > 1;
    }
}
