package com.curse2014.stormlightmod.capabilities;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class PlayerInfo implements IPlayerInfo {
    private float stormlight = 0f; // max 1000
    private int blade = -1;

    @Override
    public float getStormlight() {
        return this.stormlight;
    }

    @Override
    public void setStormlight(float stormlight) {
        if (0 <= stormlight && stormlight <= 1000) {
            this.stormlight = stormlight;
        }
    }

    @Override
    public int getBlade() {
        return this.blade;
    }

    @Override
    public void setBlade(int blade) {
        this.blade = blade;
    }

    public static IPlayerInfo getFromPlayer(PlayerEntity player){
        return player
                .getCapability(PlayerInfoProvider.PLAYER_INFO,null)
                .orElseThrow(()->new IllegalArgumentException("LazyOptional must be not empty!"));
    }
}
