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
public class LiquidStackRecipeParameter implements IRecipeParameter
{
	private final String unlocalizedName;
	private final int displayX;
	private final int displayY;
	private final boolean isPhantom;
	
	public LiquidStackRecipeParameter(String unlocalizedName, int displayX, int displayY)
	{
		this(unlocalizedName, displayX, displayY, false);
	}
	
	public LiquidStackRecipeParameter(String unlocalizedName, int displayX, int displayY, boolean isPhantom)
	{
		this.unlocalizedName = unlocalizedName;
		this.displayX = displayX;
		this.displayY = displayY;
		this.isPhantom = isPhantom;
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
		processor.onLiquidStackParameter(this);
	}
}
