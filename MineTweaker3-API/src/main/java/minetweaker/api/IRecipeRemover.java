/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api;

import minetweaker.api.item.IIngredient;

/**
 * Used to register removable mod recipes for mod machines. Once registered, it
 * will be called when minetweaker.remove() is called.
 * 
 * API status: frozen
 * 
 * @author Stan Hebben
 */
public interface IRecipeRemover
{
	/**
	 * Removes all the items matching the given ingredient.
	 *
	 * @param ingredient ingredient
	 */
	public void remove(IIngredient ingredient);
}
