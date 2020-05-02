package com.curse2014.stormlightmod.util;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import com.curse2014.stormlightmod.init.ItemInit;
import com.curse2014.stormlightmod.networking.ShardbladePacket;
import com.curse2014.stormlightmod.networking.StormlightModPacketHandler;
import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import com.curse2014.stormlightmod.objects.items.SpecialItem;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SCollectItemPacket;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RightClickEvent {
    private static ArrayList<PlayerEntity> list = new ArrayList<>();

    private static ItemStack example = new ItemStack(
            new ShardBladeItem(
                    ItemTier.DIAMOND,
                    20,
                    6,
                    new Item.Properties().group(StormlightMod.StormlightItemGroup.instance)
            ).setRegistryName("shard_blade_item")
    );

    @SubscribeEvent
    public static void rightClickEvent(PlayerInteractEvent.RightClickEmpty event) {
        PlayerEntity player = event.getPlayer();
        /*
        LazyOptional<IPlayerInfo> blade = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null);
        IPlayerInfo what = blade.orElse(null);
        World world = event.getWorld();
        ItemEntity oldFriend = (ItemEntity) (world.getEntityByID(what.getBlade()) == null ? world.getEntityByID(what.getBlade() + 1) : world.getEntityByID(what.getBlade()));
        List<Entity> a = world.getServer().getWorld(DimensionType.THE_END).getEntities().collect(Collectors.toList());
        for (Entity b : a) {
            if (b.getEntityId() == what.getBlade() || b.getEntityId() == what.getBlade() + 1) {
                oldFriend = (ItemEntity) b;
                break;
            }
        }
        System.out.println(((ClientWorld) world).getAllEntities());
        System.out.println(what.getBlade());
        System.out.println("umm " + oldFriend);
        System.out.println((world.getEntityByID(what.getBlade() + 1)));
        //ideally hsould create with more info than this, so u know its actually players blade, but i want results
        */

        ItemStack stackToAdd = new ItemStack(ItemInit.shardblade_item);
        if (!list.contains(player) && !player.inventory.hasItemStack(stackToAdd)) {
            list.add(player);
            float delay = 10000;
            delay *= (player.getHealth() / player.getMaxHealth() - ((player.isSprinting()) ? 0.5 : 0));
            if (delay < 0) {
                delay = 0;
            }
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    /*
                    Ugh this took so long to figure out what was wrong and then learn how to do it properly.
                    Basically, this event is only called on the client side, but minecraft only recognises changes to
                    inventory as legitimate if done to both sides. SO, need to create custom packet to send to server
                    which does exact same thing as addItemStackToInventory and need to do it first so stack isn't
                    'empty' whatever that means. Necessary to send player's id instead of player itself cos !player
                    instanceof ServerPLayerEntity, but yes player instanceof ClientPlayerEntity.
                    */
                    StormlightModPacketHandler.INSTANCE.sendToServer(
                            new ShardbladePacket(stackToAdd, player.getUniqueID())
                    );
                    player.addItemStackToInventory(stackToAdd);
                    list.remove(player);
                }
            }, (int) delay);
        }
    }
}
