/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.recipes;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.recipes.parameter.DoubleRecipeParameter;
import minetweaker.api.recipes.parameter.IngredientRecipeParameter;
import minetweaker.api.recipes.parameter.IntRecipeParameter;
import minetweaker.api.recipes.parameter.ItemStackRecipeParameter;
import minetweaker.api.recipes.parameter.LiquidStackRecipeParameter;
import minetweaker.api.recipes.parameter.StringRecipeParameter;

/**
 *
 * @author Stan
 */
public interface IRecipeProcessor
{
	public void onInt(IntRecipeParameter parameter, int value);
	
	public void onDouble(DoubleRecipeParameter parameter, double value);
	
	public void onString(StringRecipeParameter parameter, String value);

	public void onIngredient(IngredientRecipeParameter parameter, IIngredient value);
	
	public void onItemStack(ItemStackRecipeParameter parameter, IItemStack value);
	
	public void onLiquidStack(LiquidStackRecipeParameter parameter, ILiquidStack value);
}
