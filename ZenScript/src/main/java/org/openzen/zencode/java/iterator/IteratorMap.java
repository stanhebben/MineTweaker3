/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.iterator;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public class IteratorMap implements IJavaIterator
{
	private final IGenericType<IJavaExpression> type;
	private int iterator;

	public IteratorMap(IGenericType<IJavaExpression> type)
	{
		this.type = type;
	}

	@Override
	public void compileStart(MethodOutput output, int[] locals)
	{
		output.invokeInterface(Map.class, "entrySet", Set.class);

		iterator = output.local(Type.getType(Iterator.class));
		output.invokeInterface(Set.class, "iterator", Iterator.class);
		output.storeObject(iterator);
	}

	@Override
	public void compilePreIterate(MethodOutput output, int[] locals, Label exit)
	{
		output.loadObject(iterator);
		output.invokeInterface(
				Iterator.class,
				"hasNext",
				boolean.class);
		output.ifEQ(exit);

		output.loadObject(iterator);
		output.invokeInterface(Iterator.class, "next", Object.class);
		output.dup();

		output.invokeInterface(Map.Entry.class, "getKey", Object.class);
		output.store(type.getMapKeyType(), locals[0]);

		output.invokeInterface(Map.Entry.class, "getValue", Object.class);
		output.store(type.getMapValueType(), locals[1]);
	}

	@Override
	public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat)
	{
		output.goTo(repeat);
	}

	@Override
	public void compileEnd(MethodOutput output)
	{

	}

	@Override
	public IGenericType<IJavaExpression> getType(int i)
	{
		return i == 1 ? type.getMapValueType() : type.getMapKeyType();
	}
}
