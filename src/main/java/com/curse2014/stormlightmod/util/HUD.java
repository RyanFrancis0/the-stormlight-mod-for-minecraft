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
                int y = res.getScaledHeight() - 232;
                LazyOptional<IPlayerInfo> blade = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null);
                IPlayerInfo what = blade.orElse(null);
                float stormlight = what.getStormlight() / 1000f;
                mc.getTextureManager().bindTexture(stormlight_bar);
                mc.ingameGUI.blit(x, y, 0, 0, 204, 10);
                mc.ingameGUI.blit(x + 1, y + 1, 0, 10, (int) (202f * stormlight), 8);
                mc.getProfiler().endSection();
                /*
                if(!hasCreative) {
                    if(totalMaxMana == 0)
                        width = 0;
                    else width *= (double) totalMana / (double) totalMaxMana;
                }

                if(width == 0) {
                    if(totalMana > 0)
                        width = 1;
                    else return;
                }

                Color color = new Color(Color.HSBtoRGB(0.55F, (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 200D) * 0.5 + 1F), 1F));
                GL11.glColor4ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) (255 - color.getRed()));
                mc.renderEngine.bindTexture(manaBar);

                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.drawTexturedModalRect(x, y, 0, 0, 251, width, 5);
                GlStateManager.disableBlend();
                GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);

                 */
            }
        }
    }
}
