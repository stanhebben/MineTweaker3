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
public class IngredientRecipeParameter implements IRecipeParameter
{
	private final String unlocalizedName;
	private final boolean phantom;
	private final int displayX;
	private final int displayY;
	
	public IngredientRecipeParameter(String unlocalizedName, int displayX, int displayY)
	{
		this(unlocalizedName, false, displayX, displayY);
	}
	
	public IngredientRecipeParameter(String unlocalizedName, boolean phantom, int displayX, int displayY)
	{
		this.unlocalizedName = unlocalizedName;
		this.phantom = phantom;
		this.displayX = displayX;
		this.displayY = displayY;
	}
	
	public boolean isPhantom()
	{
		return phantom;
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
		processor.onIngredientParameter(this);
	}
}
