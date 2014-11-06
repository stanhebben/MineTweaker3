/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.recipes;

import minetweaker.api.recipes.parameter.IRecipeParameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.graphics.IImage;

/**
 *
 * @author Stan
 * @param <T> recipe object type
 */
public final class RecipeTypeDefinition<T>
{
	private final String recipeListName;
	private final String unlocalizedName;
	private final IImage backgroundImage;
	private final List<IRecipeParameter> recipeParameters;
	private final List<IRecipeParameter> unmodifiableRecipeParameters;
	private final Class<T> recipeClass;
	
	public RecipeTypeDefinition(
			String recipeListName,
			String unlocalizedName,
			IImage backgroundImage,
			List<IRecipeParameter> recipeParameters,
			Class<T> recipeClass)
	{
		this.recipeListName = recipeListName;
		this.unlocalizedName = unlocalizedName;
		this.backgroundImage = backgroundImage;
		this.recipeParameters = new ArrayList<IRecipeParameter>(recipeParameters);
		this.unmodifiableRecipeParameters = Collections.unmodifiableList(this.recipeParameters);
		this.recipeClass = recipeClass;
	}
	
	public String getRecipeListName()
	{
		return recipeListName;
	}
	
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public String getLocalizedName()
	{
		return MineTweakerAPI.game.localize(unlocalizedName);
	}
	
	public IImage getBackgroundImage()
	{
		return backgroundImage;
	}
	
	public List<IRecipeParameter> getRecipeParameters()
	{
		return unmodifiableRecipeParameters;
	}
}
