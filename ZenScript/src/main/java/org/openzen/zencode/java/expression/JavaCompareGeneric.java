/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaCompareGeneric extends AbstractJavaExpression
{
	private final IJavaExpression value;
	private final CompareType compareType;
	
	public JavaCompareGeneric(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value, CompareType compareType)
	{
		super(position, scope);
		
		this.value = value;
		this.compareType = compareType;
	}
	
	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		if (pushResult) {
			
		} else {
			value.compile(false, method);
		}
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().bool;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		IAny valueCT = value.getCompileTimeValue();
		if (valueCT == null)
			return null;
		
		int iValue = valueCT.asInt();
		return AnyBool.valueOf(compareType.evaluate(iValue));
	}

	@Override
	public void validate()
	{
		value.validate();
	}
}
