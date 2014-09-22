/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

import minetweaker.api.entity.IEntity;

/**
 *
 * @author Stan
 */
public interface IExplosion {
	public IEntity getEntity();
	
	public double getX();
	
	public double getY();
	
	public double getZ();
	
	public double getPower();
}
