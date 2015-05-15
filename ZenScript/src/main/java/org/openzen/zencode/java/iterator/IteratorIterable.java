/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.iterator;

import java.util.Iterator;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public class IteratorIterable implements IJavaIterator
{
	private final IGenericType<IJavaExpression> iteratorType;
	private int iterator;

	public IteratorIterable(IGenericType<IJavaExpression> iteratorType)
	{
		this.iteratorType = iteratorType;
	}

	@Override
	public void compileStart(MethodOutput output, int[] locals)
	{
		iterator = output.local(Type.getType(Iterator.class));
		output.invokeInterface(Iterable.class, "iterator", Iterator.class);
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
		output.checkCast(iteratorType);
		output.store(iteratorType, locals[0]);
	}

	@Override
	public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat)
	{
		output.goTo(repeat);
	}

	@Override
	public IGenericType<IJavaExpression> getType(int i)
	{
		return iteratorType;
	}

	@Override
	public void compileEnd(MethodOutput output)
	{
		output.pop();
	}
}
