package com.curse2014.stormlightmod.events;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import com.curse2014.stormlightmod.objects.items.SphereItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Timer;
import java.util.TimerTask;

//@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OnDropEvent {
    @SubscribeEvent
    public static void onDropEvent(ItemTossEvent event) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        PlayerEntity player = event.getPlayer();
        ItemStack tossed = event.getEntityItem().getItem();
        if (tossed.getItem() instanceof SphereItem) {
            if (tossed.getTag().getBoolean("inUse")) {
                tossed.getTag().putBoolean("inUse", false);
                player.removePotionEffect(((SphereItem) tossed.getItem()).effect.getPotion());
            }
        }
        if (tossed.getItem() instanceof ShardBladeItem) {
            //store blade stats with player, or store blade with player somehow
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    player.inventory.addItemStackToInventory(tossed);
                }
            }, 10000);
            event.getEntityItem().remove();
        }
    }
}
