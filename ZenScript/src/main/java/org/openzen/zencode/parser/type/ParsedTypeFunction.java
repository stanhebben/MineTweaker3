/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.TypeInstance;

/**
 *
 * @author Stan
 */
public class ParsedTypeFunction implements IParsedType
{
	private final ParsedFunctionSignature header;
	
	public ParsedTypeFunction(ParsedFunctionSignature header)
	{
		this.header = header;
	}

	@Override
	public <E extends IPartialExpression<E>> TypeInstance<E> compile(IModuleScope<E> environment)
	{
		return environment.getTypeCompiler().getFunction(environment, header.compile(environment));
	}
}
