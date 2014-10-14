/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.field;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class GeneratedField implements IField
{
	private final ZenType type;
	private final String className;
	private final String name;
	private final boolean isFinal;
	private final boolean isStatic;
	
	public GeneratedField(ZenType type, String className, String name, boolean isFinal, boolean isStatic) {
		this.type = type;
		this.className = className;
		this.name = name;
		this.isFinal = isFinal;
		this.isStatic = isStatic;
	}

	@Override
	public ZenType getType()
	{
		return type;
	}

	@Override
	public boolean isFinal()
	{
		return isFinal;
	}

	@Override
	public boolean isStatic()
	{
		return isStatic;
	}

	@Override
	public void compileStaticGet(MethodOutput output)
	{
		if (!isStatic())
			throw new UnsupportedOperationException("Cannot compile a non-static field as static");
		
		output.getStaticField(className, name, type.getSignature());
	}

	@Override
	public void compileStaticSet(MethodOutput output)
	{
		if (!isStatic())
			throw new UnsupportedOperationException("Cannot compile a non-static field as static");
		
		output.putStaticField(className, name, type.getSignature());
	}

	@Override
	public void compileInstanceGet(MethodOutput output)
	{
		if (isStatic())
			throw new UnsupportedOperationException("Cannot compile a static field as instance field");
		
		output.getField(className, name, type.getSignature());
	}

	@Override
	public void compileInstanceSet(MethodOutput output)
	{
		if (isStatic())
			throw new UnsupportedOperationException("Cannot compile a static field as instance field");
		
		output.putField(className, name, type.getSignature());
	}
}
