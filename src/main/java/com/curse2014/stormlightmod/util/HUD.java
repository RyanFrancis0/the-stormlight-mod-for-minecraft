package com.curse2014.stormlightmod.util;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfo;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
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
    private static int border_height = 10;
    private static int border_thickness = 1;

    private static void drawSurgeBar(Minecraft mc, MainWindow res, int counter) {
        mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
        int x = res.getScaledWidth() / 2 - 91;
        int i = 182;
        int j = (int)(counter * 183.0F);
        int k = res.getScaledHeight() / 20 - 32;
        mc.ingameGUI.blit(x, k, 0, 84, 182, 5);
        if (j > 0) {
            mc.ingameGUI.blit(x, k, 0, 89, j, 5);
        }
    }

    private static void drawStormlightBar(Minecraft mc, MainWindow res, IPlayerInfo playerInfo) {
        mc.getProfiler().startSection("stormlightBar");
        int x = res.getScaledWidth() / 2 - width / 2;
        int y = res.getScaledHeight() / 20;
        int percentageOfMaxStormlight = (int) (playerInfo.getStormlight() / PlayerInfo.MAX_STORMLIGHT);
        mc.getTextureManager().bindTexture(stormlight_bar);
        mc.ingameGUI.blit(x, y, 0, 0, border_width, border_height);
        mc.ingameGUI.blit(x + border_thickness, y + border_thickness, 0, 10, (border_width - 2 * border_thickness) * percentageOfMaxStormlight, border_height - 2 * border_height);
        mc.getProfiler().endSection();
    }

    @SubscribeEvent
    public static void onDrawScreen(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            MainWindow res = event.getWindow();
            PlayerEntity player = mc.player;
            if (player != null && !player.isSpectator()) {
                IPlayerInfo playerInfo = PlayerInfo.getFromPlayer(player);
                if (!player.isCreative()) {
                    drawStormlightBar(mc, res, playerInfo);
                }
                //float vCounter = playerInfo.getOrder()
                //if (vCounter)
                //    drawSurgeBar(mc, res, vCounter);
                //
            }
        }
    }
}
