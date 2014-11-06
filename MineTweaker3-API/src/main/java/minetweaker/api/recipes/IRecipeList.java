/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.recipes;

import java.util.List;

/**
 *
 * @author Stan
 * @param <T> recipe class
 */
public interface IRecipeList<T>
{
	public RecipeTypeDefinition getRecipeFormat();
	
	public List<T> getRecipes();
}
