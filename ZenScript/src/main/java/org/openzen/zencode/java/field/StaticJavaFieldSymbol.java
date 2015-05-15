/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.field;

import java.lang.reflect.Field;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class StaticJavaFieldSymbol implements IZenSymbol<IJavaExpression>
{
	private final Field field;
	private final IGenericType<IJavaExpression> fieldType;
	
	public StaticJavaFieldSymbol(Field field, IGenericType<IJavaExpression> fieldType)
	{
		this.field = field;
		this.fieldType = fieldType;
	}
	
	@Override
	public IPartialExpression<IJavaExpression> instance(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		return new GetStaticJavaFieldExpression(position, scope, field, fieldType);
	}
	
	@Override
	public IImportable<IJavaExpression> asImportable()
	{
		return null;
	}
}
