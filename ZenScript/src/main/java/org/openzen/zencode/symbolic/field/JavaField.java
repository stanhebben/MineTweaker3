/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.field;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;

/**
 *
 * @author Stan
 */
public class JavaField implements IField
{
	private final Field field;
	private final ZenType type;
	
	public JavaField(Field field, TypeRegistry types) {
		this(field, types, TypeCapture.EMPTY);
	}
	
	public JavaField(Field field, TypeRegistry types, TypeCapture capture) {
		this.field = field;
		this.type = types.getNativeType(null, field.getGenericType(), capture);
	}

	@Override
	public ZenType getType()
	{
		return type;
	}

	@Override
	public boolean isFinal()
	{
		return (field.getModifiers() & Modifier.FINAL) > 0;
	}
	
	@Override
	public boolean isStatic()
	{
		return (field.getModifiers() & Modifier.STATIC) > 0;
	}

	@Override
	public void compileStaticGet(MethodOutput output)
	{
		if (!isStatic())
			throw new UnsupportedOperationException("Cannot compile a non-static field as static");
		
		output.getStaticField(field);
	}

	@Override
	public void compileStaticSet(MethodOutput output)
	{
		if (!isStatic())
			throw new UnsupportedOperationException("Cannot compile a non-static field as static");
		
		output.putStaticField(field);
	}

	@Override
	public void compileInstanceGet(MethodOutput output)
	{
		if (isStatic())
			throw new UnsupportedOperationException("Cannot compile a static field as instance field");
		
		output.getInstanceField(field);
	}

	@Override
	public void compileInstanceSet(MethodOutput output)
	{
		if (isStatic())
			throw new UnsupportedOperationException("Cannot compile a static field as instance field");
		
		output.putInstanceField(field);
	}
}
