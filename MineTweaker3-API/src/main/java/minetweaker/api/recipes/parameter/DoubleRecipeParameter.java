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
public final class DoubleRecipeParameter implements IRecipeParameter
{
	private final String unlocalizedName;
	private final double min;
	private final double max;
	private final int displayX;
	private final int displayY;
	
	public DoubleRecipeParameter(
			String unlocalizedName,
			double min, double max,
			int displayX, int displayY)
	{
		this.unlocalizedName = unlocalizedName;
		this.min = min;
		this.max = max;
		this.displayX = displayX;
		this.displayY = displayY;
	}
	
	public double getMin()
	{
		return min;
	}
	
	public double getMax()
	{
		return max;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	@Override
	public int getDisplayX()
	{
		return displayX;
	}
	
	@Override
	public int getDisplayY()
	{
		return displayY;
	}

	@Override
	public void process(IRecipeParameterProcessor processor)
	{
		processor.onDoubleParameter(this);
	}
}
