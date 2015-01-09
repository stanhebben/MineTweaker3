/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.elements;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.ConstructorGenericParameterBound;
import org.openzen.zencode.symbolic.method.IGenericParameterBound;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 */
public class ParsedGenericBoundConstructor implements IParsedGenericBound {
	private final ParsedFunctionSignature signature;
	
	public ParsedGenericBoundConstructor(ParsedFunctionSignature signature) {
		this.signature = signature;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> IGenericParameterBound<E, T> compile(IModuleScope<E, T> scope)
	{
		return new ConstructorGenericParameterBound<E, T>(signature.compile(scope));
	}
}
