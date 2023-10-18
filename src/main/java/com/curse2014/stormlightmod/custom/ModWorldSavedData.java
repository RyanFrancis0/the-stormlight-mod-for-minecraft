package com.curse2014.stormlightmod.custom;

import com.curse2014.stormlightmod.StormlightMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Global data.
 * Such as per game only 1 player for each bondsmith spren and 1 player for each honourblade.
 */
public class ModWorldSavedData extends WorldSavedData {
    private static final String DATA_NAME = StormlightMod.MOD_ID + "_ModData";

    private static final String BONDSMITHS = "bonsmiths_";
    private static final String HONOURBLADES = "honourblades_";
    private String[] bondsmiths = {"", "", ""}; // stormfather, sibling, nightwatcher
    private String[] honourblades = {"", "", "", "", "", "", "", "", "", ""}; // windrunners->bondsmiths

    // Required constructors
    public ModWorldSavedData() {
        super(DATA_NAME);
    }

    public ModWorldSavedData(String s) {
        super(s);
    }

    @Override
    public void read(CompoundNBT nbt) {
        for (int i = 0; i < bondsmiths.length; i++) {
            bondsmiths[i] = nbt.getString(BONDSMITHS + i);
        }
        for (int i = 0; i < honourblades.length; i++) {
            honourblades[i] = nbt.getString(HONOURBLADES + i);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = new CompoundNBT();
        for (int i = 0; i < bondsmiths.length; i++) {
            nbt.putString(BONDSMITHS + i, bondsmiths[i]);
        }
        for (int i = 0; i < honourblades.length; i++) {
            nbt.putString(HONOURBLADES + i, honourblades[i]);
        }
        return nbt;
    }

    public static ModWorldSavedData get(ServerWorld world) {
        return world.getSavedData().getOrCreate(ModWorldSavedData::new, DATA_NAME);
    }

    public void setBondsmith(int i, PlayerEntity player) {
        bondsmiths[i] = player.getName().getFormattedText();
        this.setDirty(true);
    }

    public void setHonourblade(int i, PlayerEntity player) {
        honourblades[i] = player.getName().getFormattedText();
        this.setDirty(true);
    }

    public boolean isSprenFree(int i) {
        return bondsmiths[i].isEmpty();
    }

    public boolean isBladeFree(int i) {
        return honourblades[i].isEmpty();
    }

    public void unbondSpren(int i) {
        bondsmiths[i] = "";
        this.setDirty(true);
    }

    public void unbondBlade(int i) {
        honourblades[i] = "";
        this.setDirty(true);
    }

    public int isBondsmith(PlayerEntity player) {
        for (int i = 0; i < bondsmiths.length; i++) {
            if (bondsmiths[i].equals(player.getName().getFormattedText())) {
                return i;
            }
        }
        return -1;
    }

    public int hasHonourblade(PlayerEntity player) {
        for (int i = 0; i < honourblades.length; i++) {
            if (honourblades[i].equals(player.getName().getFormattedText())) {
                return i;
            }
        }
        return -1;
    }

    public int firstFreeSpren() {
        for (int i = 0; i < bondsmiths.length; i++) {
            if (bondsmiths[i].isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public int firstFreeHonourBlade() {
        for (int i = 0; i < honourblades.length; i++) {
            if (honourblades[i].isEmpty()) {
                return i;
            }
        }
        return -1;
    }
}
