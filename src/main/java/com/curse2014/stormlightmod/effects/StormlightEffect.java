package com.curse2014.stormlightmod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class StormlightEffect extends Effect {

    public StormlightEffect() {
        super(EffectType.BENEFICIAL, 0xffffff);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(@Nonnull LivingEntity entityLivingBaseIn, int amplifier) {
        if (!(entityLivingBaseIn instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
        player.heal(1);
    }

    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        //insert stuff to so start of effect
        PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
        player.setAIMoveSpeed(player.getAIMoveSpeed() + 5f);
        player.setGlowing(true);

        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        //insert stuff to do end of effect
        PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
        player.setAIMoveSpeed(player.getAIMoveSpeed() - 5f);
        player.setGlowing(false);
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

}
