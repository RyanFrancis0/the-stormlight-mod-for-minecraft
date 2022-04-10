package com.curse2014.stormlightmod.events;
import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.capabilities.IPlayerInfo;
import com.curse2014.stormlightmod.capabilities.PlayerInfoProvider;
import net.minecraft.block.Block;
import net.minecraft.block.IceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.IcePathFeature;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.KeyEvent;

@Mod.EventBusSubscriber(modid = StormlightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class keyPressEvent {
    private static boolean weirdFlag = true;//cos event run twice

    @SubscribeEvent
    public static void keyPressed(InputEvent.KeyInputEvent event) {
        //System.out.println(event.getKey());
        if (event.getKey() == KeyEvent.VK_V || event.getKey() == KeyEvent.VK_B) {
            PlayerEntity player = Minecraft.getInstance().player;
            IPlayerInfo playerInfo = player.getCapability(PlayerInfoProvider.PLAYER_INFO, null)
                    .orElse(null);
            if (playerInfo.getIdeal() > 0 && playerInfo.getStormlight() > 0) {
                playerInfo.changeStormlight(-4);
                if (playerInfo.getOrder() == 0) {
                    if (PlayerTickingEvent.flightPath == null && weirdFlag) {
                        PlayerTickingEvent.flightPath = player.getLookVec().scale(0.15 * ((event.getKey()==KeyEvent.VK_V)?1:-1));
                        player.fallDistance = 0.0F;
                    } else if (PlayerTickingEvent.flightPath != null && weirdFlag) {
                        PlayerTickingEvent.flightPath = null;
                        player.fallDistance = 0.0F;
                    }
                    weirdFlag = !weirdFlag;
                }
                /*
                In regards to slilp and slide this code (taken from livingenty's travel fuction) should be of interst
                  BlockPos blockpos = this.getPositionUnderneath();
                  float f5 = player.world.getBlockState(player.getPosition()).getSlipperiness(player.world, player.getPosition(), player);
                  f5 = 0.9;
                  float f7 = player.onGround ? f5 * 0.91F : 0.91F;
                  this.moveRelative(player.getRelevantMoveFactor(f5), p_213352_1_);
                  this.setMotion(this.func_213362_f(this.getMotion()));
                  this.move(MoverType.SELF, this.getMotion());
                  Vec3d vec3d5 = this.getMotion();
                  this.setMotion(vec3d5.x * (double)f7, d10 * (double)0.98F, vec3d5.z * (double)f7);
                obviously there r 2 ways to go from here, intercept and replace movement function or replace blocks
                beneath player. obv again first option preferable cos its u thats supposed to be the one slipping not
                entiites near u. also block slipperiness is private so no luck there.
                 */
            } else {
                //run out of stormlight, stop all stuff
                if (PlayerTickingEvent.flightPath != null) {
                    PlayerTickingEvent.flightPath = null;
                    player.fallDistance = 0.0F;
                }
            }
        }
    }
}
