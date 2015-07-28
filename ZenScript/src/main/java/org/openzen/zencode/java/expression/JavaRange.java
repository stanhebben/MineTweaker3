/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyRange;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.runtime.Range;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaRange extends AbstractJavaExpression
{
	private final IJavaExpression from;
	private final IJavaExpression to;
	
	public JavaRange(
			CodePosition position,
			IMethodScope<IJavaExpression> scope,
			IJavaExpression from,
			IJavaExpression to)
	{
		super(position, scope);
		
		this.from = from;
		this.to = to;
	}

	@Override
	public void compileValue(MethodOutput method)
	{
		method.newObject(Range.class);
		method.dup();
		from.compileValue(method);
		to.compileValue(method);
		method.invokeSpecial(Range.class, "<init>", void.class, int.class, int.class);
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		from.compileStatement(method);
		to.compileStatement(method);
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().range;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		IAny fromValue = from.getCompileTimeValue();
		if (fromValue == null)
			return null;
		
		IAny toValue = to.getCompileTimeValue();
		if (toValue == null)
			return null;
		
		return new AnyRange(fromValue.asInt(), toValue.asInt());
	}

	@Override
	public void validate()
	{
		
	}
}
