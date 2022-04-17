package com.curse2014.stormlightmod.util;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HUD {
    private static ResourceLocation stormlight_bar = new ResourceLocation(StormlightMod.MOD_ID, "textures/gui/stormlight_bar.png");
    private static int width = 182;
    private static int border_width = 204;
    private static int border_thickness = 1;

    @SubscribeEvent
    public static void onDrawScreen(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            MainWindow res = event.getWindow();
            PlayerEntity player = mc.player;
            assert player != null;
            if (!player.isSpectator() && !player.isCreative()) {
                mc.getProfiler().startSection("stormlightBar");
                int x = res.getScaledWidth() / 2 - width / 2;
                int y = res.getScaledHeight() / 20;
                IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
                int percentageOfMaxStormlight = (int) (playerInfo.getStormlight() / PlayerInfo.MAX_STORMLIGHT);
                mc.getTextureManager().bindTexture(stormlight_bar);
                mc.ingameGUI.blit(x, y, 0, 0, border_width, 10);
                mc.ingameGUI.blit(x + border_thickness, y + border_thickness, 0, 10, (border_width - 2 * border_thickness) * percentageOfMaxStormlight, 8);
                mc.getProfiler().endSection();
            }
        }
    }
}
