/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMethod;
import org.openzen.zencode.annotations.ZenOperator;

/**
 * Block patterns are similar to ingredients, except that they match blocks instead.
 * 
 * @author Stan Hebben
 */
@ZenClass("minetweaker.block.IBlockPattern")
public interface IBlockPattern {
	/**
	 * Returns all possible blocks that could be matched by this pattern.
	 * Patterns may be more restrictive than just these blocks.
	 * 
	 * @return 
	 */
	@ZenMethod("blocks")
	public List<IBlock> getPossibleBlocks();
	
	/**
	 * Checks if the given block matches this pattern.
	 * 
	 * @param block block to check
	 * @return match status
	 */
	@ZenOperator(OperatorType.CONTAINS)
	public boolean matches(IBlock block);
	
	/**
	 * Performs an or with this pattern. An or pattern will match if any of its
	 * subpatterns match.
	 * 
	 * @param pattern
	 * @return 
	 */
	@ZenOperator(OperatorType.OR)
	public IBlockPattern or(IBlockPattern pattern);
	
	@ZenGetter("displayName")
	public String getDisplayName();
	
}
