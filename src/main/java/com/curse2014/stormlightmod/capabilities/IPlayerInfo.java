package com.curse2014.stormlightmod.capabilities;

import net.minecraft.entity.item.ItemEntity;

import java.util.UUID;

public interface IPlayerInfo {
    //get and change info
    public float getStormlight();

    public void setStormlight(float stormlight);

    public int getBlade();

    public void setBlade(int blade);
}
