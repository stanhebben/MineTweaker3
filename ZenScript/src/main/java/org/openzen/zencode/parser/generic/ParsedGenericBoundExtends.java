/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.generic;

import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.generic.ExtendsGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 */
public class ParsedGenericBoundExtends implements IParsedGenericBound
{
	private final IParsedType type;
	
	public ParsedGenericBoundExtends(IParsedType type)
	{
		this.type = type;
	}
	
	public IParsedType getType()
	{
		return type;
	}

	@Override
	public <E extends IPartialExpression<E>> IGenericParameterBound<E> compile(IModuleScope<E> scope, ITypeVariable<E> typeVariable)
	{
		return new ExtendsGenericParameterBound<>(this, scope);
	}
}
