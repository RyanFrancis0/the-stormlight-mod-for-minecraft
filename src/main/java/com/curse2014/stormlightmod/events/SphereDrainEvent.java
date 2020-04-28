package com.curse2014.stormlightmod.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;

@Cancelable
@Event.HasResult
public class SphereDrainEvent extends PlayerEvent {
    private final ItemUseContext context;

    public SphereDrainEvent(ItemUseContext context) {
        super(context.getPlayer());
        this.context = context;
    }

    @Nonnull
    public ItemUseContext getContext()
        {
            return this.context;
        }

    @Override
    public void setResult(Result value) {
        super.setResult(Result.ALLOW);
    }
}
