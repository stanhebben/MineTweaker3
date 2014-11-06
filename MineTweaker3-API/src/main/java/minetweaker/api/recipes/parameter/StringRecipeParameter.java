/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.recipes.parameter;

import minetweaker.api.MineTweakerAPI;

/**
 *
 * @author Stan
 */
public final class StringRecipeParameter implements IRecipeParameter
{
	private final String unlocalizedName;
	private final String unlocalizedPattern;
	private final int displayX;
	private final int displayY;
	
	public StringRecipeParameter(String unlocalizedName, String unlocalizedPattern, int displayX, int displayY)
	{
		this.unlocalizedName = unlocalizedName;
		this.unlocalizedPattern = unlocalizedPattern;
		this.displayX = displayX;
		this.displayY = displayY;
	}
	
	public String getUnlocalizedPattern()
	{
		return unlocalizedPattern;
	}
	
	public String getText(String value)
	{
		return MineTweakerAPI.game.localize(unlocalizedPattern).replace("${value}", value);
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
		processor.onStringParameter(this);
	}
}
