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
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.symbolic.scope.IMethodScope;

/**
 *
 * @author Stan
 */
public class IteratorList
	implements IJavaIterator
{
	private final IMethodScope<IJavaExpression, IJavaType> scope;
	private final IJavaType iteratorType;
	private int iterator;

	public IteratorList(IJavaType iteratorType, IMethodScope<IJavaExpression, IJavaType> scope)
	{
		this.scope = scope;
		this.iteratorType = iteratorType;
	}

	@Override
	public void compileStart(MethodOutput output, int[] locals)
	{
		iterator = output.local(Type.getType(Iterator.class));
		output.invokeInterface(Iterable.class, "iterator", Iterator.class);
		output.storeObject(iterator);
		output.iConst0();
		output.storeInt(locals[0]);
	}

	@Override
	public void compilePreIterate(MethodOutput output, int[] locals, Label exit)
	{
		output.dup();
		output.invokeInterface(
				Iterator.class,
				"hasNext",
				boolean.class);
		output.ifEQ(exit);

		output.dup();
		output.invokeInterface(Iterator.class, "next", Object.class);
		output.store(iteratorType.toASMType(), locals[1]);
	}

	@Override
	public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat)
	{
		output.iinc(locals[0]);
		output.goTo(repeat);
	}

	@Override
	public void compileEnd(MethodOutput output)
	{
		output.pop();
	}

	@Override
	public IJavaType getType(int i)
	{
		return i == 0 ? iteratorType.getScope().getTypeCompiler().getInt(scope) : iteratorType;
	}
}
