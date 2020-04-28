package com.curse2014.stormlightmod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
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
        if (player.getHealth() < player.getMaxHealth()) {
            entityLivingBaseIn.heal(0.5F);
        }
        player.getFoodStats().setFoodSaturationLevel(0);
        //faster,. then in armour increase jumping and
        /* is possible to change player speed and gravity, best look back at
        https://www.minecraftforge.net/forum/topic/29927-the-entity-attribute-system-how-to-manipulate-it-in-code/
        player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier();
        IAttributeInstance iattributeinstance = entityLivingBaseIn.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (iattributeinstance.getModifier(PRINTING_SPEED_BOOST_ID) != null) {
            iattributeinstance.removeModifier(LivingEntity.SWI);
            player.getAttributes().applyAttributeModifiers();
        }
        private static final UUID pegasusBootsMoveBonusUUID = UUID.fromString("36A0FC05-50EB-460B-8961-615633A6D813");
		private static final AttributeModifier pegasusBootsMoveBonus = (new AttributeModifier(pegasusBootsMoveBonusUUID, "Pegasus Boots Speed Bonus", 0.3D, 2)).setSaved(false);
        player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(pegasusBootsMoveBonus);
        iattributeinstance.applyModifier(new AttributeModifier(UUID.fromString("dfg"), "Pegasus Boots Speed Bonus", 0.3D, 2));

         */
    }

    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        //insert stuff to so start of effect
        PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
        player.setGlowing(true);
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        //insert stuff to do end of effect
        PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
        player.setGlowing(false);
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

}
