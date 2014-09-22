/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.minecraft;

import minetweaker.api.block.IBlock;
import minetweaker.api.item.IItemStack;
import minetweaker.mc1710.block.MCBlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 *
 * @author Stan
 */
public class MCBlock {
	private MCBlock() {}
	
	/**
	 * Retrieves the block from an item stack.
	 * 
	 * @param itemStack
	 * @return 
	 */
	public static Block getBlock(IItemStack itemStack) {
		return ((MCBlockDefinition) itemStack.asBlock().getDefinition()).getInternalBlock();
	}
	
	/**
	 * Retrieves the Minecraft block from the given MineTweaker block.
	 * 
	 * @param block block to convert
	 * @return 
	 */
	public static Block getBlock(IBlock block) {
		return ((MCBlockDefinition) block.getDefinition()).getInternalBlock();
	}
	
	/**
	 * Converts the given block into a tile entity. Will return the tile entity
	 * of the given block if it's a Minecraft block, or the generic MineTweaker
	 * tile entity if it's a MineTweaker defined block.
	 * 
	 * @param block
	 * @return 
	 */
	public static TileEntity getTileEntity(IBlock block) {
		return TileEntity.createAndLoadEntity(MineTweakerMC.getNBTCompound(block.getTileData()));
	}
}
