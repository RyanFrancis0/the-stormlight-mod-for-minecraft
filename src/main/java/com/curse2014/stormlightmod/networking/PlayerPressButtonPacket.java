package com.curse2014.stormlightmod.networking;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import com.curse2014.stormlightmod.custom.orders.Order;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.awt.event.KeyEvent;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerPressButtonPacket {
        private UUID playerID;
        private int button;

        public PlayerPressButtonPacket(PlayerEntity player, int b) {
            this.playerID = player.getUniqueID();
            this.button = b;
        }

        public PlayerPressButtonPacket(PacketBuffer buf) {
            this.playerID = buf.readUniqueId();
            this.button = buf.readInt();
        }

        public void encode(PacketBuffer buf) {
            buf.writeUniqueId(this.playerID);
            buf.writeInt(this.button);
        }

    /**
     * TODO: This is semi vulnerable to player sending packets.
     * @param context
     */
    public void handle(Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                //when do this with custom entities dont need player uuid, world keeps synced perfect track of entitieIds
                ServerPlayerEntity player = context.get().getSender().world.getServer().getPlayerList()
                        .getPlayerByUUID(playerID);
                if (player != null) {
                    Order order = PlayerInfo.getFromPlayer(player).getOrder();
                    switch (button) {
                        case KeyEvent.VK_V:
                            order.vPressed();
                            break;
                        case KeyEvent.VK_X:
                            order.xPressed();
                            break;
                    }
                }
            });
            context.get().setPacketHandled(true);
        }
    }

