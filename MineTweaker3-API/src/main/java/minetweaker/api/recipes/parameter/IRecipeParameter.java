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
public interface IRecipeParameter
{
	public String getUnlocalizedName();
	
	public int getDisplayX();
	
	public int getDisplayY();
	
	public void process(IRecipeParameterProcessor processor);
}
