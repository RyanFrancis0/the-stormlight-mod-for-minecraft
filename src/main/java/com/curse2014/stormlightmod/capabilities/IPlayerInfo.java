package com.curse2014.stormlightmod.capabilities;

import net.minecraft.entity.item.ItemEntity;

import java.util.UUID;

public interface IPlayerInfo {
    //get and change info
    public float getStormlight();

    public void changeStormlight(float stormlight);

    public int getBlade();

    public void setBlade(int blade);

    public int getIdeal();

    public void oathAccepted();

    public int getOrder();

    public boolean canDoThing(int thing);
}
