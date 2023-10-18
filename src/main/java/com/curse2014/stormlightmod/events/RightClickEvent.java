package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.networking.ShardbladePacket;
import com.curse2014.stormlightmod.networking.StormlightModPacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RightClickEvent {
    @SubscribeEvent
    public static void rightClickEvent(PlayerInteractEvent.RightClickEmpty event) {
        PlayerEntity player = event.getPlayer();
        IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
        ItemStack stackToAdd = playerInfo.getCurrentBlade();
        if (stackToAdd != null && !player.inventory.hasItemStack(stackToAdd)) {
            float delay = playerInfo.getBlade() == 0 ? 0 : Math.abs((float) Math.floor(10000 * (player.getHealth() / player.getMaxHealth() - ((player.isSprinting()) ? 0.5 : 0))));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    /*
                    Ugh this took so long to figure out what was wrong and then learn how to do it properly.
                    Basically, this event is only called on the client side so only have ClientSidePlayer and
                    ClientSideWorld, but Minecraft only recognises changes to inventory as legitimate if done to
                    both sides. SO, need to create custom packet to send to server to run addItemStackToInventory server
                    side and need to do it first so stack isn't 'empty' whatever that means. Necessary to send player's
                    id instead of player itself cos again its a ClientPlayerEntity.
                    */
                    StormlightModPacketHandler.INSTANCE.sendToServer(
                            new ShardbladePacket(stackToAdd, player.getUniqueID())
                    );
                    player.inventory.add(player.inventory.currentItem, stackToAdd);
                }
            }, (int) delay);
        }
    }
}
