package com.curse2014.stormlightmod.networking;

import com.curse2014.stormlightmod.StormlightMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class StormlightModPacketHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    private static int ID = 0;
    public static int nextID() {return ID++;}
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(StormlightMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            s -> true,
            s -> true
    );
    public static void registerMessage() {
        INSTANCE.registerMessage(
                nextID(),
                ShardbladePacket.class,
                ShardbladePacket::encode,
                ShardbladePacket::new,
                ShardbladePacket::handle
        );
        //in order to do other packets copy above exactly but replace packet class (obviously)
    }
}
