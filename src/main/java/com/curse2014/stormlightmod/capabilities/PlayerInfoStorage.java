package com.curse2014.stormlightmod.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PlayerInfoStorage implements Capability.IStorage<IPlayerInfo> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerInfo> capability, IPlayerInfo instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        //tag.putVarType("varName", instance.getVar());
        //do for each info being stored
        tag.putInt("blade", instance.getBlade());
        tag.putInt("ideal", instance.getIdeal());
        tag.putInt("order", instance.getOrder());
        tag.putFloat("stormlight", instance.getStormlight());
        //System.out.println(tag.getFloat("stormlight"));
        //System.out.println(instance.getIdeal());
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerInfo> capability, IPlayerInfo instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        //instance.setVar(tag.getVarType("varName"));
        //do for each info being retrieved
        //!!! Must read out same order put in
        instance.setBlade(tag.getInt("blade"));
        int ideal = tag.getInt("ideal");
        while (instance.getIdeal() < ideal) {
            instance.oathAccepted();
        }
        instance.setOrder(tag.getInt("order"));
        instance.setStormlight(tag.getFloat("stormlight"));
        //System.out.println(instance.getStormlight());
    }
}
