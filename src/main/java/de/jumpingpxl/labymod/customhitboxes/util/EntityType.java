package de.jumpingpxl.labymod.customhitboxes.util;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ExperienceBottleEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

public enum EntityType {
	UNKNOWN,
	PLAYER,
	ANIMAL,
	MOB,
	DROP,
	THROWABLE;

	public static EntityType fromEntity(Entity entity) {
		if (entity instanceof PlayerEntity) {
			return PLAYER;
		}

		if ((entity instanceof AnimalEntity || entity instanceof AmbientEntity
				|| entity instanceof WaterMobEntity || entity instanceof AbstractVillagerEntity)
				&& !(entity instanceof HoglinEntity)) {
			return ANIMAL;
		}

		if (entity instanceof MobEntity || entity instanceof HoglinEntity) {
			return MOB;
		}

		if (entity instanceof ItemEntity || entity instanceof ExperienceBottleEntity) {
			return DROP;
		}

		if (entity instanceof ProjectileEntity || entity instanceof AreaEffectCloudEntity) {
			return THROWABLE;
		}

		return UNKNOWN;
	}
}
