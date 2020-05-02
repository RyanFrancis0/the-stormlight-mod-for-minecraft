package com.curse2014.stormlightmod.networking;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

//for other packets copy exactly but change vars and do stuff area below
public class ShardbladePacket {
        private final ItemStack stack;
        private final UUID playerID;

        public ShardbladePacket(ItemStack stack, UUID playerID) {
            this.stack = stack;
            this.playerID = playerID ;
        }

        public ShardbladePacket(PacketBuffer buf) {
            this.stack = buf.readItemStack();
            this.playerID = buf.readUniqueId();
        }

        public void encode(PacketBuffer buf) {
            buf.writeItemStack(stack);
            buf.writeUniqueId(playerID);
        }

        public void handle(Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                //do stuff area
                ServerPlayerEntity player = context.get().getSender().world.getServer().getPlayerList()
                        .getPlayerByUUID(playerID);
                player.addItemStackToInventory(stack);
            });
            context.get().setPacketHandled(true);
        }
    }

