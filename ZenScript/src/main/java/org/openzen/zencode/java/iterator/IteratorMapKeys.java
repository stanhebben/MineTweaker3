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
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.java.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class IteratorMapKeys implements IJavaIterator
{
	private final IJavaType type;
	private int iterator;

	public IteratorMapKeys(IJavaType type)
	{
		this.type = type;
	}

	@Override
	public void compileStart(MethodOutput output, int[] locals)
	{
		output.invokeInterface(Map.class, "keySet", Set.class);

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
		output.store(type.getMapKeyType().toASMType(), locals[0]);
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
	public IJavaType getType(int i)
	{
		return type.getMapKeyType();
	}
}
