/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.recipes;

/**
 *
 * @author Stan
 */
public interface IRecipe
{
	public void processValues(IRecipeProcessor processor);
	
	public <T> T fill(Class<T> recipeClass);
}
