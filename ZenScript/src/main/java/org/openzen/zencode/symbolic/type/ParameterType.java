/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ParameterType<E extends IPartialExpression<E>> extends TypeDefinition<E>
{
	private final IModuleScope<E> scope;
	private final ITypeVariable<E> parameter;
	private final boolean nullable;
	
	public ParameterType(IModuleScope<E> scope, ITypeVariable<E> parameter)
	{
		this(scope, parameter, false);
	}
	
	public ParameterType(IModuleScope<E> scope, ITypeVariable<E> parameter, boolean nullable)
	{
		this.scope = scope;
		this.parameter = parameter;
		this.nullable = nullable;
		
		for (IGenericParameterBound<E> bound : parameter.getBounds()) {
			if (bound.getBound() != null)
				addSuperclass(bound.getBound());
			
			if (bound.getMember() != null)
				addMember(bound.getMember());
		}
	}
}
