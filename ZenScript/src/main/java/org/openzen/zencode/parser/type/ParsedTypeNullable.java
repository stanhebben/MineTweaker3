/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 * Contains a parsed nullable type (type?).
 *
 * @author Stan Hebben
 */
public class ParsedTypeNullable implements IParsedType
{
	private final CodePosition position;
	private final IParsedType baseType;

	public ParsedTypeNullable(CodePosition position, IParsedType baseType)
	{
		this.position = position;
		this.baseType = baseType;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 T compile(IModuleScope<E, T> scope)
	{
		T cBaseType = baseType.compile(scope);
		T type = cBaseType.nullable();
		if (type == null) {
			scope.getErrorLogger().errorCannotBeNullable(position, cBaseType);
			type = scope.getTypeCompiler().getAny(scope);
		}
		return type;
	}
	
	@Override
	public String toString()
	{
		return baseType + "?";
	}
}
