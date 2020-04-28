package com.curse2014.stormlightmod.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerInfoStorage implements Capability.IStorage<IPlayerInfo> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerInfo> capability, IPlayerInfo instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        //tag.putVarType("varName", instance.getVar());
        //do for each info being stored
        tag.putInt("blade", instance.getBlade());
        tag.putFloat("stormlight", instance.getStormlight());
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerInfo> capability, IPlayerInfo instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        //instance.setVar(tag.getVarType("varName"));
        //do for each info being retrieved
        instance.setBlade(tag.getInt("blade"));
        instance.setStormlight(tag.getFloat("stormlight"));
    }
}
