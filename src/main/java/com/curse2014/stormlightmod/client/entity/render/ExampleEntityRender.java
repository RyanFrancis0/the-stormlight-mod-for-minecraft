package com.curse2014.stormlightmod.client.entity.render;

import com.curse2014.stormlightmod.StormlightMod;
import com.curse2014.stormlightmod.entities.ExampleEntity;
import com.curse2014.stormlightmod.client.entity.model.ExampleEntityModel;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class ExampleEntityRender extends MobRenderer<ExampleEntity, ExampleEntityModel<ExampleEntity>> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(StormlightMod.MOD_ID,
			"textures/entity/example_entity.png");
	
	public ExampleEntityRender(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new ExampleEntityModel<ExampleEntity>(), 0.5f);
	}
	
	@Override
	public ResourceLocation getEntityTexture(ExampleEntity entity) {
		return TEXTURE;
	}
}
