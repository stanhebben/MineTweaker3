/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.action;

/**
 *
 * API Status: frozen
 * 
 * @author Stan Hebben
 */
public abstract class UndoableAction implements IUndoableAction
{
	@Override
	public final boolean canUndo()
	{
		return true;
	}
	
	@Override
	public boolean isSilent()
	{
		return false;
	}
}
