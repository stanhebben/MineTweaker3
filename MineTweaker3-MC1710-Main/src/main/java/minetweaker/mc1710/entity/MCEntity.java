/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.mc1710.entity;

import minetweaker.api.entity.IEntity;
import minetweaker.api.math.Vector3d;
import minetweaker.api.minecraft.MCWorld;
import minetweaker.api.world.IDimension;
import net.minecraft.entity.Entity;

/**
 *
 * @author Stan
 */
public class MCEntity implements IEntity {
	public static MCEntity valueOf(Entity entity) {
		return entity == null ? null : new MCEntity(entity);
	}
	
	public static Entity getEntity(IEntity entity) {
		if (entity instanceof MCEntity) {
			return ((MCEntity) entity).entity;
		} else {
			return null;
		}
	}
	
	private final Entity entity;
	
	private MCEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public IDimension getDimension() {
		return MCWorld.getDimension(entity.worldObj);
	}

	@Override
	public double getX() {
		return entity.posX;
	}

	@Override
	public double getY() {
		return entity.posY;
	}

	@Override
	public double getZ() {
		return entity.posZ;
	}

	@Override
	public Vector3d getPosition() {
		return new Vector3d(entity.posX, entity.posY, entity.posZ);
	}

	@Override
	public void setPosition(Vector3d position) {
		entity.setPosition(position.getX(), position.getY(), position.getZ());
	}
}
