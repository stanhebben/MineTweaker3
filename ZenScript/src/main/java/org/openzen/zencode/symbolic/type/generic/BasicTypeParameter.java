/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.generic;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;

/**
 *
 * @author Stan
 */
public class BasicTypeParameter<E extends IPartialExpression<E>> implements ITypeVariable<E>
{
	@Override
	public List<IGenericParameterBound<E>> getBounds()
	{
		return Collections.emptyList();
	}
}
