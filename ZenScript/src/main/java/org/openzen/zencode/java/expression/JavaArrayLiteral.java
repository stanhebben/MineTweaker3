/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyArray;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaArrayLiteral extends AbstractJavaExpression
{
	private final List<IJavaExpression> values;
	private final IGenericType<IJavaExpression> type;
	
	public JavaArrayLiteral(CodePosition position, IMethodScope<IJavaExpression> scope, IGenericType<IJavaExpression> type, List<IJavaExpression> values)
	{
		super(position, scope);
		
		this.type = type;
		this.values = values;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		if (pushResult) {
			// TODO: array or list?
			boolean isArray = false;
			TODO
		} else {
			for (IJavaExpression value : values) {
				value.compile(false, method);
			}
		}
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return type;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		List<IAny> ctValues = new ArrayList<>();
		for (IJavaExpression value : values) {
			IAny ctValue = value.getCompileTimeValue();
			if (ctValue == null)
				return null;
			
			ctValues.add(ctValue);
		}
		
		return new AnyArray(ctValues);
	}

	@Override
	public void validate()
	{
		
	}
}
