/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

import minetweaker.api.data.IData;
import minetweaker.api.player.IPlayer;
import minetweaker.api.render.IRenderer;
import minetweaker.api.math.Ray;
import minetweaker.api.world.IBlockGroup;
import zenscript.annotations.Optional;
import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenGetter;
import zenscript.annotations.ZenMethod;

/**
 * Block interface. Used to interact with blocks in the world as well as a 
 * means to define new blocks.
 * 
 * @author Stan Hebben
 */
@ZenClass("minetweaker.block.IBlock")
public interface IBlock extends IBlockPattern {
	/**
	 * Gets the block definition.
	 * 
	 * @return block definition
	 */
	@ZenGetter("definition")
	public IBlockDefinition getDefinition();
	
	/**
	 * Gets the block metadata.
	 * 
	 * @return block meta
	 */
	@ZenGetter("meta")
	public int getMeta();
	
	/**
	 * Gets the block data.
	 * 
	 * @return block data
	 */
	@ZenGetter("data")
	public IData getTileData();
	
	/**
	 * Checks if this block is an air block. Air blocks are assumed to be
	 * replaceable by anything and passable.
	 * 
	 * @return true for an air block, false otherwise.
	 */
	@ZenGetter("isAir")
	public boolean isAir();
	
	/**
	 * Checks if this block is a normal cube. Normal cubes have all their sides
	 * completely filled, are opaque and solid.
	 * 
	 * @return true for a normal cube, false otherwise
	 */
	@ZenGetter("isNormalCube")
	public boolean isNormalCube();
	
	/**
	 * Gets the light emitted by this block. Returns an ARGB value, with
	 * A = light strength. Color might or might not be ignored depending on the
	 * platform it is running on.
	 * 
	 * (Color value = A << 24 | R << 16 | G << 8 | B)
	 * 
	 * @return block light value
	 */
	@ZenGetter("blockLight")
	public int getBlockLight();
	
	/**
	 * Gets the blast resistance of the block at the given position.
	 * 
	 * @param blocks block group
	 * @param x block x
	 * @param y block y
	 * @param z block z
	 * @param explosion explosion data
	 * @return explosion resistance for the given explosion
	 */
	@ZenMethod
	public float getBlastResistance(IBlockGroup blocks, int x, int y, int z, IExplosion explosion);
	
	/**
	 * Gets the hardness of the block at the given position.
	 * 
	 * @param blocks block group
	 * @param x block x
	 * @param y block y
	 * @param z block z
	 * @param player player for which to calculate hardness
	 * @return hardness value
	 */
	@ZenMethod
	public float getHardness(IBlockGroup blocks, int x, int y, int z, @Optional IPlayer player);

	/**
	 * Renders the given block in the world.
	 * 
	 * @param blocks block group
	 * @param x block x
	 * @param y block y
	 * @param z block z
	 * @param renderer renderer
	 */
	@ZenMethod
	public void renderWorld(IBlockGroup blocks, int x, int y, int z, IRenderer renderer);
	
	/**
	 * Renders the given block in the inventory.
	 * 
	 * @param renderer renderer
	 */
	@ZenMethod
	public void renderInventory(IRenderer renderer);
	
	/**
	 * Calculates the collission of the ray with this block.
	 * 
	 * @param blocks
	 * @param x
	 * @param y
	 * @param z
	 * @param ray
	 * @return 
	 */
	@ZenMethod
	public BlockCollision collisionRayTrace(IBlockGroup blocks, int x, int y, int z, Ray ray);
}
