package com.curse2014.stormlightmod.capabilities;

import com.curse2014.stormlightmod.custom.orders.Order;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;

import java.util.List;
import java.util.UUID;

public interface IPlayerInfo {
    //get and change info
    float getStormlight();

    void changeStormlight(float stormlight);

    void setStormlight(float stormlight);

    int getBlade();

    List<ItemStack> getBlades();

    void addBlade(ItemStack blade);

    void setBlade(int blade);

    void bondBlade(ItemStack blade, boolean isAlive);

    void unbondBlade(ItemStack blade);

    boolean isBondedTo(ItemStack blade);

    boolean isMyBlade(ItemStack blade);

    ItemStack getCurrentBlade();

    int getIdeal();
    
    void setIdeal(int ideal);

    void oathAccepted();

    Order getOrder();

    int getOrderNumber();

    void setOrder(int order);

    INBT convertToNBT();

    void setValuesFromNBT(INBT nbt);

    boolean canDoThing(int thing);
}
