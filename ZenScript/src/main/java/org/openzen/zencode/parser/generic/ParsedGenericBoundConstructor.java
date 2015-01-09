/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.generic;

import org.openzen.zencode.parser.elements.IParsedGenericBound;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.generic.ConstructorGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.scope.IModuleScope;

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
	public <E extends IPartialExpression<E>> IGenericParameterBound<E> compile(IModuleScope<E> scope)
	{
		return new ConstructorGenericParameterBound<E>(signature.compile(scope));
	}
}
