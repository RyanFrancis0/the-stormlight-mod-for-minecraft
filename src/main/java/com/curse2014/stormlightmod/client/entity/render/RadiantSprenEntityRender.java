package com.curse2014.stormlightmod.client.entity.render;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.client.entity.model.ExampleEntityModel;
import com.curse2014.stormlightmod.client.entity.model.RadiantSprenEntityModel;
import com.curse2014.stormlightmod.entities.ExampleEntity;
import com.curse2014.stormlightmod.entities.RadiantSprenEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RadiantSprenEntityRender extends MobRenderer<RadiantSprenEntity, RadiantSprenEntityModel<RadiantSprenEntity>> {

	protected static final ResourceLocation RADIANT_SPREN = new ResourceLocation(StormlightMod.MOD_ID,
			"textures/entity/example_entity.png");

	public RadiantSprenEntityRender(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new RadiantSprenEntityModel<RadiantSprenEntity>(), 0.5f);
	}

	public ResourceLocation getEntityTexture(RadiantSprenEntity entity) {
		return RADIANT_SPREN;
	}
}
