package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OnDropEvent {
    @SubscribeEvent
    public static void onDropEvent(ItemTossEvent event) {
        if (event.getEntityItem().getItem().getItem() instanceof ShardBladeItem) {
            ItemEntity ie = event.getEntityItem();
            if (PlayerInfo.getFromPlayer(event.getPlayer()).isBondedTo(ie.getItem())) {
                ie.remove();
            }
        }
    }
}
