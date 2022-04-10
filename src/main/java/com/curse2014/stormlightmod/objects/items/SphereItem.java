package com.curse2014.stormlightmod.objects.items;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import com.curse2014.stormlightmod.init.EffectInit;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

//import static com.curse2014.stormlightmod.init.EffectInit.STORMLIGHT_EFFECT;

public class SphereItem extends Item {
    private int type;
    private int size;
    private int value; //monetary, should be func of type and size
    private int maxStormlight = 4800;//should also be some func of type & size, when add diff types and sizes
    //game is 20 ticks/second, so 4800 == 4 minutes of stormlight in 1 sphere (whatever this sphere is);
    private static int normalDrainRate = 1; // sphere's are always slowly going dun
    private static int usingDrainRate = 20;
    private static int chargeRate = 100; //how fast should recharge during hightstorm.
    private static String inUse = "inUse";
    // public EffectInstance effect = new EffectInstance(EffectInit.stormlight, this.maxStormlight);
//    private Consumer<PlayerEntity> uh = new Consumer<PlayerEntity>() {
//        @Override
//        public void accept(PlayerEntity playerEntity) {
//            playerEntity.sendBreakAnimation(playerEntity.getActiveHand());
//        }
//    };
    private int count = 0;
    private static int numberOfSpheresInWorld = 0;

    //diamond chip
    public SphereItem(Properties properties) {
        super(properties.maxDamage(4800).defaultMaxDamage(4800));
        this.type = 1;
        this.size = 1;
        this.value = 1;
        numberOfSpheresInWorld++;
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
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        CompoundNBT compoundnbt = itemStack.getOrCreateTag();
        compoundnbt.putBoolean(inUse, !compoundnbt.getBoolean(inUse));
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        count += 1;
        if (count == usingDrainRate) {
            PlayerEntity player = ((PlayerEntity) entityIn);
            IPlayerInfo playerInfo = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null)
                    .orElse(null);
            count = 0;
            CompoundNBT compoundnbt = stack.getOrCreateTag();
            if (!player.isAlive()) {
                compoundnbt.putBoolean(inUse, false);
            }
            //System.out.println(this.getStormlight(stack));
            int newDamage = stack.getDamage();
            if (compoundnbt.getBoolean(inUse)) {
                if (this.getStormlight(stack) <= usingDrainRate) {
                    compoundnbt.putBoolean(inUse, false);
                    newDamage = stack.getMaxDamage() - 1;//set to 1 stormlight/damag
                } else {
                    newDamage += usingDrainRate;
                    playerInfo.changeStormlight(usingDrainRate);
                    //System.out.println(playerInfo.getStormlight());
                }
            } else if (hasEffect(stack)) {
                newDamage += normalDrainRate;
            }
            if (worldIn.isThundering()) {
                if (chargeRate < stack.getDamage()) {
                    newDamage -= chargeRate;
                } else {
                    newDamage = 0;
                }
            }
            stack.setDamage(newDamage);
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
        if (!hasEffect(stack)) {
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
