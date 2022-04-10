package com.curse2014.stormlightmod.util;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HUD {

    private static ResourceLocation stormlight_bar = new ResourceLocation(StormlightMod.MOD_ID, "textures/gui/stormlight_bar.png");

    @SubscribeEvent
    public static void onDrawScreen(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            MainWindow res = event.getWindow();
            PlayerEntity player = mc.player;
            assert player != null;
            if (!player.isSpectator() && !player.isCreative()) {
                mc.getProfiler().startSection("stormlightBar");
                int width = 182;
                int x = res.getScaledWidth() / 2 - width / 2;
                int y = res.getScaledHeight() / 20;
                IPlayerInfo playerInfo = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null).orElse(null);
                float stormlight = (playerInfo == null) ?  0 : playerInfo.getStormlight() / 1000f;
                mc.getTextureManager().bindTexture(stormlight_bar);
                mc.ingameGUI.blit(x, y, 0, 0, 204, 10);
                mc.ingameGUI.blit(x + 1, y + 1, 0, 10, (int) (202f * stormlight), 8);
                mc.getProfiler().endSection();
            }
        }
    }
}
