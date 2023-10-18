package com.curse2014.stormlightmod.capabilities;

import com.curse2014.stormlightmod.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public class PlayerInfoStorage implements Capability.IStorage<IPlayerInfo> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerInfo> capability, IPlayerInfo instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        //do for each info being stored
        tag.putInt("blade", instance.getBlade());
        tag.putInt("ideal", instance.getIdeal());
        tag.putInt("order", instance.getOrderNumber());
        tag.putFloat("stormlight", instance.getStormlight());
        List<ItemStack> blades = instance.getBlades();
        tag.putInt("nBlades", blades.size());
        for (int i = 0; i < blades.size(); i++) {
            tag.put("blade_" + i, blades.get(i).serializeNBT());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerInfo> capability, IPlayerInfo instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        //do for each info being retrieved
        //!!! Must read out same order put in
        instance.setBlade(tag.getInt("blade"));
        instance.setIdeal(tag.getInt("ideal"));
        instance.setOrder(tag.getInt("order"));
        instance.setStormlight(tag.getFloat("stormlight"));
        for (int i = 0; i < tag.getInt("nBlades"); i++) {
            ItemStack blade = new ItemStack(ItemInit.shardblade_item);
            blade.deserializeNBT(tag.getCompound("blade_" + i));
            instance.addBlade(blade);
        }
    }
}
