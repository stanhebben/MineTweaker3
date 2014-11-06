/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

/**
 *
 * @author Stan
 */
public enum BlockSide
{
	POSX,
	NEGX,
	POSY,
	NEGY,
	POSZ,
	NEGZ;
	
	public static final BlockSide TOP = POSY;
	public static final BlockSide BOTTOM = NEGY;
	public static final BlockSide NORTH = NEGZ;
	public static final BlockSide EAST = POSX;
	public static final BlockSide SOUTH = POSZ;
	public static final BlockSide WEST = NEGX;
}
