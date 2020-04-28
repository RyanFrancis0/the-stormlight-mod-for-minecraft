package com.curse2014.stormlightmod.util;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import com.curse2014.stormlightmod.objects.items.SpecialItem;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RightClickEvent {
    private static ArrayList<PlayerEntity> list = new ArrayList<>();
    @SubscribeEvent
    public static void rightClickEvent(PlayerInteractEvent.RightClickEmpty event) {
        PlayerEntity player = event.getPlayer();
        LazyOptional<IPlayerInfo> blade = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null);
        IPlayerInfo what = blade.orElse(null);
        /*player.serializeNBT().put;
        //AnvilChunkLoader#readWorldEntity}. Use {@link #writeUnlessPassenger}
        player.writeUnlessPassenger()

        World world = event.getWorld();
        world.getEntityByID(what.getBlade().hashCode());
        world.getEntityByID()
        ItemEntity a = new ItemEntity(null, null);
        a.read();
         */
        World world = event.getWorld();
        ItemEntity oldFriend = (ItemEntity) (world.getEntityByID(what.getBlade()) == null ? world.getEntityByID(what.getBlade() + 1) : world.getEntityByID(what.getBlade()));
        System.out.println(((ClientWorld) world).getAllEntities());
        System.out.println(what.getBlade());
        System.out.println("umm " + oldFriend);
        System.out.println((world.getEntityByID(what.getBlade() + 1)));
        Timer timer = new Timer();
        //ideally hsould create with more info than this, so u know its actually players blade, but i want results
        //also check if already in playeS inventory
        //player.world.getChunkAt(player.getPosition()).addEntity(oldFriend);
        player.inventory.addItemStackToInventory(oldFriend.getItem());/*
        if (!list.contains(player)) {
            list.add(player);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    list.remove(player);
                    player.inventory.addItemStackToInventory(
                            new ItemStack(
                                    /*new ShardBladeItem(
                                            ItemTier.DIAMOND,
                                            15,
                                            5,
                                            new Item.Properties().group(StormlightMod.StormlightItemGroup.instance)
                                    )//
                            )
                    );
                }
            }, 10000);
        }*/
        /*right so presumably want to do this right, but r ni want results more than i want right
        oldFriend.revive();
        player.addItemStackToInventory(oldFriend.getItem());
        */

        /*
        goddman it, move blade nbt tag to player, u cant summon an item from nothing



        ItemStack tossed = event.getItemStack();
        CompoundNBT compoundnbt = tossed.getTag();
        //if blade bonded to a person,
        if (compoundnbt != null && !compoundnbt.getString("player").equals("")) {

        }
         */
    }
}
