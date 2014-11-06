/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.game;

import java.util.List;
import minetweaker.api.block.IBlock;
import minetweaker.api.block.IBlockDefinition;
import minetweaker.api.block.IBlockFactory;
import minetweaker.api.entity.IEntityDefinition;
import minetweaker.api.item.IItemDefinition;
import minetweaker.api.liquid.ILiquidDefinition;
import minetweaker.api.world.IBiome;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMethod;

/**
 * Game interface. Used to obtain general game information.
 * 
 * @author Stan Hebben
 */
@ZenClass("minetweaker.game.IGame")
public interface IGame {
	/**
	 * Retrieves the item definitions in this game.
	 * 
	 * @return game items
	 */
	@ZenGetter("items")
	public List<IItemDefinition> getItems();
	
	/**
	 * Retrieves the block definitions in this game.
	 * 
	 * @return block definitions
	 */
	@ZenGetter("blocks")
	public List<IBlockDefinition> getBlocks();
	
	/**
	 * Retrieves the liquids in this game.
	 * 
	 * @return game liquids
	 */
	@ZenGetter("liquids")
	public List<ILiquidDefinition> getLiquids();
	
	/**
	 * Retrieves the biomes in this game.
	 * 
	 * @return game biomes
	 */
	@ZenGetter("biomes")
	public List<IBiome> getBiomes();
	
	/**
	 * Retrieves the entities in this game.
	 * 
	 * @return game entities
	 */
	@ZenGetter("entities")
	public List<IEntityDefinition> getEntities();
	
	/**
	 * Sets a localization value.
	 * 
	 * @param key localization key
	 * @param value localization value
	 */
	@ZenMethod
	public void setLocalization(String key, String value);
	
	/**
	 * Sets a localization value.
	 * 
	 * @param lang language
	 * @param key localization key
	 * @param value  localization value
	 */
	@ZenMethod
	public void setLocalization(String lang, String key, String value);
	
	/**
	 * Gets a localized string.
	 * 
	 * @param key localization key
	 * @return localized value
	 */
	@ZenMethod
	public String localize(String key);
	
	/**
	 * Gets a localized string.
	 * 
	 * @param key localization key
	 * @param lang language
	 * @return localized value
	 */
	@ZenMethod
	public String localize(String key, String lang);
	
	/**
	 * Gets the block definition for the given ID, or creates a new one if it
	 * doesn't already exist in the current world.
	 * 
	 * @param id block id
	 * @return block definition
	 */
	@ZenMethod
	public IBlockDefinition makeDefinition(String id);
	
	/**
	 * Registers a simple block with a given ID. Fails if the given ID already
	 * exists.
	 * 
	 * @param meta meta value to be used
	 * @param block block to register
	 */
	@ZenMethod
	public void registerBlock(int meta, IBlock block);
	
	/**
	 * Registers an advanced block with a given ID. Fails if the given ID
	 * alreadly exists.
	 * 
	 * @param factory block factory to register
	 */
	@ZenMethod
	public void registerAdvancedBlock(IBlockFactory factory);
}
