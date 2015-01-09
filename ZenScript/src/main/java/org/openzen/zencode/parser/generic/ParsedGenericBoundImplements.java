/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.generic;

import org.openzen.zencode.parser.elements.IParsedGenericBound;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.ImplementsGenericParameterBound;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 */
public class ParsedGenericBoundImplements implements IParsedGenericBound {
	private final IParsedType type;
	
	public ParsedGenericBoundImplements(IParsedType type) {
		this.type = type;
	}

	public IParsedType getType()
	{
		return type;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> IGenericParameterBound<E, T> compile(IModuleScope<E, T> scope)
	{
		return new ImplementsGenericParameterBound<E, T>(this, scope);
	}
}
