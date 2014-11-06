/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

import minetweaker.api.entity.IEntity;

/**
 * Unfinished.
 * 
 * @author Stan
 */
public final class BlockCollision
{
	private final double distance;
	private final double x;
	private final double y;
	private final double z;
	private final IBlock block;
	private final BlockSide blockSide;
	private final IEntity entity;
	
	public BlockCollision(double distance, double x, double y, double z, IBlock block, BlockSide blockSide, IEntity entity)
	{
		this.distance = distance;
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
		this.blockSide = blockSide;
		this.entity = entity;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
	
	public IBlock getBlock()
	{
		return block;
	}
	
	public BlockSide getBlockSide()
	{
		return blockSide;
	}
	
	public IEntity getEntity()
	{
		return entity;
	}
}
