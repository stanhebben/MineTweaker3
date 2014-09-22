/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.block;

import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenGetter;

/**
 * Blocks definitions provide additional information about blocks. Never
 * implement your own block definition - always generate one from IGame.
 * 
 * @author Stan Hebben
 */
@ZenClass("minetweaker.block.IBlockDefinition")
public interface IBlockDefinition {
	/**
	 * Returns the block ID. A combination of block ID, block meta and block
	 * data should provide all necessary data to construct a block.
	 * 
	 * @return block ID
	 */
	@ZenGetter("id")
	public String getId();
	
	/**
	 * Returns the unlocalized name of this block. Used to determine the 
	 * localized name by appending ".name" to it and using that as translation
	 * key. Individual blocks may have their own names as well.
	 * 
	 * @return unlocalized name
	 */
	@ZenGetter("unlocalizedName")
	public String getUnlocalizedName();
	
	/**
	 * Returns the display name of this block. Translated using the localization
	 * system.
	 * 
	 * @return localized name
	 */
	@ZenGetter("displayName")
	public String getDisplayName();
}
