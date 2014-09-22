/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import minetweaker.util.ArrayUtil;

/**
 * Implements an or (|) operation on two or more blocks.
 * 
 * @author Stan Hebben
 */
public final class BlockPatternOr implements IBlockPattern {
	private final IBlockPattern[] elements;
	
	/**
	 * Constructs a block pattern consisting of the given elements.
	 * 
	 * @param elements 
	 */
	public BlockPatternOr(IBlockPattern... elements) {
		this.elements = elements;
	}
	
	// ####################################
	// ### IBlockPattern implementation ###
	// ####################################
	
	@Override
	public List<IBlock> getBlocks() {
		List<IBlock> blocks = new ArrayList<IBlock>();
		for (IBlockPattern pattern : elements) {
			blocks.addAll(pattern.getBlocks());
		}
		return blocks;
	}

	@Override
	public boolean matches(IBlock block) {
		for (IBlockPattern pattern : elements) {
			if (pattern.matches(block))
				return true;
		}
		
		return false;
	}

	@Override
	public String getDisplayName() {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (IBlockPattern pattern : elements) {
			if (first) {
				first = false;
			} else {
				result.append(" | ");
			}
			
			result.append(pattern.getDisplayName());
		}
		
		return result.toString();
	}

	@Override
	public IBlockPattern or(IBlockPattern pattern) {
		return new BlockPatternOr(ArrayUtil.append(elements, pattern));
	}
	
	// #############################
	// ### Object implementation ###
	// #############################
	
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 29 * hash + Arrays.deepHashCode(this.elements);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BlockPatternOr other = (BlockPatternOr) obj;
		
		if (!Arrays.deepEquals(this.elements, other.elements)) {
			return false;
		}
		return true;
	}
}
