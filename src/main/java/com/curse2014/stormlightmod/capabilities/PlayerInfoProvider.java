package com.curse2014.stormlightmod.capabilities;


import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerInfoProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(IPlayerInfo.class)
    public static Capability<IPlayerInfo> PLAYER_INFO;
    //may need to make above final and  = null?
    //private LazyOptional<IPlayerInfo> instnce = LazyOptional.of(PLAYER_INFO::getDefaultInstance);

    //private IPlayerInfo instnce = PLAYER_INFO.getDefaultInstance();
    private  LazyOptional<IPlayerInfo> instnce = LazyOptional.of(PlayerInfo::new);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_INFO ? instnce.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return PLAYER_INFO.getStorage().writeNBT(PLAYER_INFO, instnce
                .orElseThrow(()->new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        PLAYER_INFO.getStorage().readNBT(PLAYER_INFO, instnce
                .orElseThrow(()->new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
