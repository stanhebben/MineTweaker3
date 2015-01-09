/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 * Represents a parsed type.
 * 
 * @author Stan Hebben
 */
public interface IParsedType
{
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> 
		T compile(IModuleScope<E, T> environment);
}
