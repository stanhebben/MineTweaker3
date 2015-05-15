/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaTernary extends AbstractJavaExpression
{
	private final IJavaExpression condition;
	private final IJavaExpression ifTrue;
	private final IJavaExpression ifFalse;
	
	public JavaTernary(
			CodePosition position,
			IMethodScope<IJavaExpression> scope,
			IJavaExpression condition,
			IJavaExpression ifTrue,
			IJavaExpression ifFalse)
	{
		super(position, scope);
		
		this.condition = condition;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		Label exit = new Label();
		Label lblTrue = new Label();
		
		condition.compileIf(lblTrue, method);
		ifFalse.compile(pushResult, method);
		method.goTo(exit);
		method.label(lblTrue);
		ifTrue.compile(pushResult, method);
		method.label(exit);
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return ifFalse.getType().unify(getScope(), ifTrue.getType());
	}

	@Override
	public IAny getCompileTimeValue()
	{
		IAny value = condition.getCompileTimeValue();
		if (value == null)
			return null;
		
		if (value.asBool()) {
			return ifTrue.getCompileTimeValue();
		} else {
			return ifFalse.getCompileTimeValue();
		}
	}

	@Override
	public void validate()
	{
		
	}
}
