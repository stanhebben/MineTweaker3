/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.recipes.parameter;

/**
 *
 * @author Stan
 */
public interface IRecipeParameterProcessor
{
	public void onIntParameter(IntRecipeParameter parameter);
	
	public void onDoubleParameter(DoubleRecipeParameter parameter);
	
	public void onStringParameter(StringRecipeParameter parameter);
	
	public void onIngredientParameter(IngredientRecipeParameter parameter);
	
	public void onItemStackParameter(ItemStackRecipeParameter parameter);
	
	public void onLiquidStackParameter(LiquidStackRecipeParameter parameter);
}
