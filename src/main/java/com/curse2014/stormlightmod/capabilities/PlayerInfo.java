package com.curse2014.stormlightmod.capabilities;

import com.curse2014.stormlightmod.custom.orders.Order;
import com.curse2014.stormlightmod.custom.orders.Skybreaker;
import com.curse2014.stormlightmod.init.ItemInit;
import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class PlayerInfo implements IPlayerInfo {
    // https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2811327-how-to-store-items-in-nbt
    // I think I just generate itemstack once when player first summons blade. store item stack in player when generated, or picked up and bonded.
    // then i put it in and remove it from inventory but it's not permanently gone till unbonded->itementity->despawn
    //could store in itemstack tag whether its dead or not. or rather than set custom var in item stack why not
    public static int MAX_STORMLIGHT = 10000;
    public static int CHARGE_RATE = 1000;

    private float stormlight = 0f; // max 1000
    private int blade = -1;
    private List<ItemStack> bondedBlades = new ArrayList<>();//each blade has a name, and it has a type.
    private int ideal = 4;
    private Order order;
    private int orderInt = -1;

    @Override
    public float getStormlight() {
        return this.stormlight;
    }

    @Override
    public void changeStormlight(float stormlight) {
        this.setStormlight(this.getStormlight() + stormlight);
    }

    @Override
    public void setStormlight(float stormlight) {
        if (this.getIdeal() > 0) {
            this.stormlight = stormlight;
            if (this.stormlight < 0) {
                this.stormlight = 0;
            } else if (this.stormlight > MAX_STORMLIGHT) {
                this.stormlight = MAX_STORMLIGHT;
            }
        }
    }

    @Override
    public int getBlade() {
        return this.blade;
    }

    @Override
    public List<ItemStack> getBlades() {
        return this.bondedBlades;
    }

    public void addBlade(ItemStack blade) {
        if (this.bondedBlades == null) {
            this.bondedBlades = new ArrayList<>();
        }
        this.bondedBlades.add(blade);
    }

    @Override
    public void setBlade(int blade) {
        this.blade = blade;
    }

    public void bondBlade(ItemStack blade, boolean isAlive) {
        if (isAlive) {
            bondedBlades.set(0, ItemStack.EMPTY);
        } else {
            if (this.bondedBlades.isEmpty()) {
                this.bondedBlades.add(ItemStack.EMPTY);
            }
            this.bondedBlades.add(blade);
        }
    }

    public void unbondBlade(ItemStack blade) {
        if (isMyBlade(blade)) {
            this.bondedBlades.set(0, ItemStack.EMPTY);
        }
        this.bondedBlades.remove(blade);
    }

    public boolean isBondedTo(ItemStack blade) {
        return this.bondedBlades.contains(blade);
    }

    public boolean isMyBlade(ItemStack blade) {
        return this.bondedBlades.indexOf(blade) == 0;
    }

    public ItemStack getCurrentBlade() {
        if (this.blade >= 0) {
            this.blade = this.blade + 1 == this.bondedBlades.size() ? 0 : this.blade + 1;
            if (this.blade == 0 && this.bondedBlades.get(0).equals(ItemStack.EMPTY) && this.blade + 1 < this.bondedBlades.size()) {
                this.blade++;
            }
            return this.bondedBlades.get(this.blade);
        }
        return null;
    }

    @Override
    public int getIdeal() {//so get level
        return this.ideal;
    }

    @Override
    public void setIdeal(int ideal) {
        this.ideal = ideal;
    }

    @Override
    public void oathAccepted() {
        switch (++this.ideal) {
            case 1:
                //if (!this.order instanceof Skybreakers) {
                //
                //}
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    @Override
    public Order getOrder() {
        if (this.order == null) {
            switch (this.orderInt) {
                case 0:
                    this.order = new Skybreaker();
                case 1:
                    this.order = new Skybreaker();
                case 2:
                    this.order = new Skybreaker();
                case 3:
                    this.order = new Skybreaker();
                case 4:
                    this.order = new Skybreaker();
                case 5:
                    this.order = new Skybreaker();
                case 6:
                    this.order = new Skybreaker();
                case 7:
                    this.order = new Skybreaker();
                case 8:
                    this.order = new Skybreaker();
                default:
                    this.order = new Skybreaker();
            }
        }
        return this.order;
    }

    public int getOrderNumber() {
        return this.orderInt;
    }

    @Override
    public void setOrder(int order) {
        this.orderInt = order;
    }

    @Override
    public boolean canDoThing(int thing) {
        if (thing == 0) {
            if (blade != -1) {
                return true;
            }
        } else if (thing == 1) { //or has honourblade as one of blades
            //can use 1st surge
            //this.order.firstsurge
        } else if (thing == 2) { //or has honourblade as one of blades
            //can use 2nd surge
            //this.order.secondsurge
        } else if (thing == 3) {
            //can summon plate
        }
        return true;//change to false
    }

    public INBT convertToNBT() {
        return PlayerInfoProvider.PLAYER_INFO.getStorage().writeNBT(PlayerInfoProvider.PLAYER_INFO, this, null);
    }

    public void setValuesFromNBT(INBT nbt) {
        PlayerInfoProvider.PLAYER_INFO.getStorage().readNBT(PlayerInfoProvider.PLAYER_INFO, this, null, nbt);
    }

    public static IPlayerInfo getFromPlayer(PlayerEntity player){
        return player
                .getCapability(PlayerInfoProvider.PLAYER_INFO,null)
                .orElseThrow(()->new IllegalArgumentException("LazyOptional must be not empty!"));
    }
}
