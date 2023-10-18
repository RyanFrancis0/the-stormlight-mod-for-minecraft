package com.curse2014.stormlightmod.custom.orders;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.custom.Surge;
import com.curse2014.stormlightmod.events.PlayerTickingEvent;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class Skybreaker extends Order {
    public String getName() {
        return "Skybreakers";
    }

    @Override
    public void on_tick(PlayerEntity player) {
        IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
        if (playerInfo.getIdeal() < 2) {
            return;
        }
        super.on_tick(player);
        float stormlight = playerInfo.getStormlight();
        if (stormlight <= 0 || stormlight % 20 != 0) {
            this.isXActive = false;
            this.isVActive = false;
            return;
        }
        //actually replace with bar
        if (this.isVPressed) {
            this.isVPressed = false;
            this.isVActive = !this.isVActive;
        }
        //replace with bar
        if (this.isXPressed) {
            //see HUD
            this.isXPressed = false;
            this.isXActive = !this.isXActive;
        }
        if (this.isVActive) {
            Surge.gravitation(player);
        }
        if (playerInfo.getStormlight() > 0 && this.isXActive) {
            Surge.division(player);
        }
    }
}
