package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.CapabilityHandler;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerChatEvent {
    @SubscribeEvent
    public static void onChatEvent(ServerChatEvent event) {
        if (event.getMessage().equals("Life before death, strength before weakness, journey before destination.")) {
            PlayerEntity player = event.getPlayer();
            IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
            if (playerInfo.getIdeal() == 0) {
                playerInfo.setIdeal(1);
                CapabilityHandler.sendClientCapabilityInfo(player);
            }
        }
    }
}
