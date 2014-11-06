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
public final class IntRecipeParameter
{
	private final String unlocalizedName;
	private final int min;
	private final int max;
	
	public IntRecipeParameter(String unlocalizedName, int min, int max)
	{
		this.unlocalizedName = unlocalizedName;
		this.min = min;
		this.max = max;
	}
	
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public int getMin()
	{
		return min;
	}
	
	public int getMax()
	{
		return max;
	}
}
