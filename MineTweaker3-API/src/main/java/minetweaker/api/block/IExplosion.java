/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

import minetweaker.api.entity.IEntity;

/**
 * Describes information about an explosion.
 * 
 * @author Stan Hebben
 */
public interface IExplosion {
	public IEntity getSourceEntity();
	
	public double getSourceX();
	
	public double getSourceY();
	
	public double getSourceZ();
	
	public double getExplosionPower();
}
