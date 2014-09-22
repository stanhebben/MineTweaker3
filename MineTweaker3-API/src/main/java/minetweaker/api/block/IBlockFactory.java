/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

import minetweaker.api.data.IData;
import minetweaker.api.world.IBlockGroup;
import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenGetter;
import zenscript.annotations.ZenMethod;

/**
 * Block factories are used to construct blocks. Factories may return the same
 * block multiple times or return a different block every time.
 * 
 * @author Stan Hebben
 */
@ZenClass("minetweaker.block.IBlockFactory")
public interface IBlockFactory {
	/**
	 * Retrieves the factory's block definition. There is a 1:1 relationship
	 * between block definitions and factories, and this method must always
	 * return the same block definition.
	 * 
	 * @return block definition for this factory
	 */
	@ZenGetter("definition")
	public IBlockDefinition getDefinition();
	
	/**
	 * Constructs a new world block. The block is tied to that specific location
	 * and should not be used for other locations.
	 * 
	 * @param blocks
	 * @param x
	 * @param y
	 * @param z
	 * @param meta
	 * @param data
	 * @return 
	 */
	@ZenMethod
	public IBlock createForWorld(IBlockGroup blocks, int x, int y, int z, int meta, IData data);
	
	/**
	 * Constructs a new unplaced block. Such block should not be placed in the
	 * world.
	 * 
	 * @param meta block meta value
	 * @param data block data contents
	 * @return constructed block
	 */
	@ZenMethod
	public IBlock create(int meta, IData data);
}
