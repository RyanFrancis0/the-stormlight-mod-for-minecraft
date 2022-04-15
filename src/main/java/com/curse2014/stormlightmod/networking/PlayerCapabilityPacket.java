package com.curse2014.stormlightmod.networking;

import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

//for other packets copy exactly but change vars and do stuff area below
public class PlayerCapabilityPacket {
        private UUID playerID;
        private CompoundNBT capabilityNBT;

        public PlayerCapabilityPacket(ServerPlayerEntity player, CompoundNBT nbt) {
            this.playerID = player.getUniqueID() ;
            this.capabilityNBT = nbt;
        }

        public PlayerCapabilityPacket(PacketBuffer buf) {
            this.playerID = buf.readUniqueId();
            this.capabilityNBT = buf.readCompoundTag();
        }

        public void encode(PacketBuffer buf) {
            buf.writeUniqueId(this.playerID);
            buf.writeCompoundTag(this.capabilityNBT);
        }

        public void handle(Supplier<NetworkEvent.Context> context) {
            NetworkDirection a = context.get().getDirection();
            context.get().enqueueWork(() -> {
                //do stuff area
                //when do this with custom entities dont need player uuid, world keeps synced perfect track of entititeIds
                //PlayerEntity player = context.get().getSender();//.world.getPlayerByUuid(this.playerID);
                ClientPlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    IPlayerInfo cap = PlayerInfo.getFromPlayer(player);
                    cap.setValuesFromNBT(this.capabilityNBT);
                }
            });
            context.get().setPacketHandled(true);
        }
    }

