package com.curse2014.stormlightmod.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RadiantSprenEntity extends TameableEntity {
	public RadiantSprenEntity(EntityType<? extends TameableEntity> type, World worldIn) {
		super(type, worldIn);
		this.setInvulnerable(true);
		this.moveController = new FlyingMovementController(this, 10, false);
	}

	public RadiantSprenEntity(EntityType<? extends TameableEntity> type, World worldIn, PlayerEntity playerEntity) {
		this(type, worldIn);
		this.setTamedBy(playerEntity);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
		this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
	}

	@Nullable
	@Override
	public AgeableEntity createChild(AgeableEntity ageableEntity) {
		return null;
	}

	public boolean attackEntityAsMob(Entity entityIn) {
		return false;
	}

	public void setSitting(boolean b) {}

	public boolean canBeLeashedTo(PlayerEntity p_184652_1_) {
		return false;
	}

	public boolean shouldAttackEntity(LivingEntity p_142018_1_, LivingEntity p_142018_2_) {
		return false;
	}

	protected PathNavigator createNavigator(World worldIn) {
		FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
		flyingpathnavigator.setCanOpenDoors(false);
		flyingpathnavigator.setCanSwim(true);
		flyingpathnavigator.setCanEnterDoors(true);
		return flyingpathnavigator;
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.6F;
	}

	public boolean canAttack(LivingEntity p_213336_1_) { return false; }

	@Override
	public void livingTick() {
		super.livingTick();
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue((double)0.4F);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.2F);
	}
}
