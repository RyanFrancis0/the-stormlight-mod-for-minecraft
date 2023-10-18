package com.curse2014.stormlightmod.events;
import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.networking.PlayerPressButtonPacket;
import com.curse2014.stormlightmod.networking.StormlightModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.KeyEvent;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyPressEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void keyPressed(InputEvent.KeyInputEvent event) {
        if (event.getKey() == KeyEvent.VK_V) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                StormlightModPacketHandler.INSTANCE.sendToServer(new PlayerPressButtonPacket(player, KeyEvent.VK_V));
                PlayerInfo.getFromPlayer(player).getOrder().vPressed();
            }
        }
        if (event.getKey() == KeyEvent.VK_X) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                StormlightModPacketHandler.INSTANCE.sendToServer(new PlayerPressButtonPacket(player, KeyEvent.VK_V));
                PlayerInfo.getFromPlayer(player).getOrder().xPressed();
            }
        }
    }
}
