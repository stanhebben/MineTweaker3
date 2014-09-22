/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.world;

import minetweaker.api.block.IBlock;
import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenGetter;
import zenscript.annotations.ZenMethod;

/**
 * Represents a block group. A block group can be the world, a moving chunk or
 * anything else that fits the block group interface.
 * 
 * @author Stan Hebben
 */
@ZenClass("minetweaker.world.IBlockGroup")
public interface IBlockGroup {
	/**
	 * Gets the dimension this block group is in. Returns itself if this block
	 * group is the dimension.
	 * 
	 * @return dimension
	 */
	@ZenGetter("dimension")
	public IDimension getDimension();
	
	/**
	 * Retrieves the block at the given position. Never returns null and never
	 * throws an IndexOutOfBoundsException, but may return an air block.
	 * 
	 * @param x position x
	 * @param y position y
	 * @param z position z
	 * @return block at the given position
	 */
	@ZenMethod
	public IBlock getBlock(int x, int y, int z);
	
	/**
	 * Sets the block at the given position.
	 * 
	 * @param x block x position
	 * @param y block y position
	 * @param z block z position
	 * @param block block to be set
	 * @param render indicates if the block should be re-rendered
	 */
	@ZenMethod
	public void setBlock(int x, int y, int z, IBlock block, boolean render);
}
