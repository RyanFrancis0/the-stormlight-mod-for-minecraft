package com.curse2014.stormlightmod.objects.items;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

class SphereConstants {
    private static int seconds = 20;
    private static int minutes = 60 * seconds;
    /**
     * All about how long stormlight will last AND cost of surgebinding AND rarity of these stones.
     * Huh gonna be real tricky to calibrate.
     * Cos want players to value mining
     * @param polestone
     * @param denomination
     * @return
     */
    protected static int getMaxStormlight(SphereItem.POLESTONE polestone, SphereItem.DENOMINATION denomination) {
        int max = 0;
        switch (polestone) {
            case DIAMOND:
                switch (denomination) {
                    case CHIP:
                        max = 3 * seconds;
                    case MARK:
                        max = 10 * seconds;
                    case BROAM:
                        max = minutes;
                }
            case GARNET:
            case HELIODOR:
            case TOPAZ:
                switch (denomination) {
                    case CHIP:
                        max = 10 * seconds;
                    case MARK:
                        max = minutes;
                    case BROAM:
                        max = 3 * minutes;
                }
            case RUBY:
            case SMOKESTONE:
            case ZIRCON:
                switch (denomination) {
                    case CHIP:
                        max = 30 * seconds;
                    case MARK:
                        max = 2 * minutes;
                    case BROAM:
                        max = 6 * minutes;
                }
            case AMETHYST:
            case SAPPHIRE:
                switch (denomination) {
                    case CHIP:
                        max = minutes;
                    case MARK:
                        max = 5 * minutes;
                    case BROAM:
                        max = 10 * minutes;
                }
            case EMERALD:
                switch (denomination) {
                    case CHIP:
                        max = 2 * minutes;
                    case MARK:
                        max = 8 * minutes;
                    case BROAM:
                        max = 20 * minutes;
                }
        }
        return max * PlayerInfo.CHARGE_RATE;
    }

    protected static int getNormalDrainRate(SphereItem.POLESTONE polestone, SphereItem.DENOMINATION denomination) {
        return seconds;
    }

    protected static int getMaxSoulcastUses(SphereItem.DENOMINATION denomination) {
        switch (denomination) {
            case CHIP:
                return 5;
            case MARK:
                return 10;
            case BROAM:
                return 15;
        }
        return 0;
    }

    protected static int getMonetaryValue(SphereItem.POLESTONE polestone, SphereItem.DENOMINATION denomination) {
        switch (polestone) {
            case DIAMOND:
                switch (denomination) {
                    case CHIP:
                        return 1;
                    case MARK:
                        return 5;
                    case BROAM:
                        return 20;
                }
            case GARNET:
            case HELIODOR:
            case TOPAZ:
                switch (denomination) {
                    case CHIP:
                        return 5;
                    case MARK:
                        return 20;
                    case BROAM:
                        return 100;
                }
            case RUBY:
            case SMOKESTONE:
            case ZIRCON:
                switch (denomination) {
                    case CHIP:
                        return 10;
                    case MARK:
                        return 50;
                    case BROAM:
                        return 200;
                }
            case AMETHYST:
            case SAPPHIRE:
                switch (denomination) {
                    case CHIP:
                        return 25;
                    case MARK:
                        return 125;
                    case BROAM:
                        return 500;
                }
            case EMERALD:
                switch (denomination) {
                    case CHIP:
                        return 50;
                    case MARK:
                        return 250;
                    case BROAM:
                        return 1000;
                }
        }
        return -1;
    }
}

public class SphereItem extends Item {
    public enum POLESTONE {
        DIAMOND,
        GARNET,
        HELIODOR,
        TOPAZ,
        RUBY,
        SMOKESTONE,
        ZIRCON,
        AMETHYST,
        SAPPHIRE,
        EMERALD
    }

    public enum DENOMINATION {
        CHIP,
        MARK,
        BROAM
    }

    public static int CHARGE_RATE = 1000; //how fast should sphere recharge during hightstorm/perpendicularity.
    private final String IN_USE = "inUse";
    private final String TICK = "tick";
    private final String CRACKS = "cracks";
    public static int soulcast_uses = 5;
    public POLESTONE polestone;
    public DENOMINATION denomination;
    private int normalDrainRate; // sphere's are always slowly going dun
    private int soulcastUses;
    private int monetaryValue;

    public SphereItem(Properties properties, POLESTONE polestone, DENOMINATION denomination) {
        super(properties.maxDamage(SphereConstants.getMaxStormlight(polestone, denomination)).defaultMaxDamage(SphereConstants.getMaxStormlight(polestone, denomination)));
        this.polestone = polestone;
        this.denomination = denomination;
        this.normalDrainRate = SphereConstants.getNormalDrainRate(polestone, denomination);
        this.soulcastUses = SphereConstants.getMaxSoulcastUses(denomination);
        this.monetaryValue = SphereConstants.getMonetaryValue(polestone, denomination);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        CompoundNBT compoundnbt = itemStack.getOrCreateTag();
        compoundnbt.putBoolean(IN_USE, !compoundnbt.contains(IN_USE) || !compoundnbt.getBoolean(IN_USE));
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * Ah stormlight calculations
     * @param stack
     * @param worldIn
     * @param entityIn
     * @param itemSlot
     * @param isSelected
     */
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        if (!compoundnbt.contains(TICK)) {
            compoundnbt.putInt(TICK, 0);
            return;
        }
        int tick = compoundnbt.getInt(TICK) + 1;
        compoundnbt.putInt(TICK, tick);
        if (tick % this.normalDrainRate != 0) {
            return;
        }
        boolean isInUse;
        if (!compoundnbt.contains(IN_USE)) {
            isInUse = false;
            compoundnbt.putBoolean(IN_USE, isInUse);
        } else {
            isInUse = compoundnbt.getBoolean(IN_USE);
        }
        int newDamage = stack.getDamage();
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) entityIn);
            if (isInUse) {
                if (!player.isAlive()) {
                    compoundnbt.putBoolean(IN_USE, false);
                } else if (this.hasEffect(stack)) {
                    IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
                    float playerStormlight = playerInfo.getStormlight();
                    float amountToAddToPlayer = (playerStormlight + PlayerInfo.CHARGE_RATE > PlayerInfo.MAX_STORMLIGHT) ? PlayerInfo.MAX_STORMLIGHT - playerStormlight : PlayerInfo.CHARGE_RATE;
                    playerInfo.changeStormlight(amountToAddToPlayer);
                    newDamage += (amountToAddToPlayer - normalDrainRate);
                }
            }
        } else if (isInUse) {
            compoundnbt.putBoolean(IN_USE, false);
        }
        if (hasEffect(stack)) {
            newDamage += normalDrainRate;
        }
        if (worldIn.isThundering()) {
            if (CHARGE_RATE < stack.getDamage()) {
                newDamage -= CHARGE_RATE;
            } else {
                newDamage = 0;
            }
        }
        stack.setDamage(newDamage);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!hasEffect(stack)) {
            tooltip.add(new StringTextComponent("This is a dun sphere, find a Highstorm to recharge it!"));
        }
        tooltip.add(new StringTextComponent("Can be used " + this.soulcastUses + " more times for soulcasting"));
        tooltip.add(new StringTextComponent("Worth " + this.monetaryValue + " Diamond Chips"));
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
