/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.minecraft;

import minetweaker.api.block.BlockSide;
import net.minecraftforge.common.util.ForgeDirection;

/**
 *
 * @author Stan
 */
public class MCDirection {
	private MCDirection() {}
	
	public static BlockSide fromForge(ForgeDirection direction) {
		switch (direction) {
			case UP:
				return BlockSide.TOP;
			case DOWN:
				return BlockSide.BOTTOM;
			case NORTH:
				return BlockSide.NORTH;
			case EAST:
				return BlockSide.EAST;
			case SOUTH:
				return BlockSide.SOUTH;
			case WEST:
				return BlockSide.WEST;
			default:
				throw new AssertionError("Invalid value");
		}
	}
}
