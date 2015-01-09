/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionCast extends ParsedExpression {
	private final ParsedExpression value;
	private final IParsedType type;
	
	public ParsedExpressionCast(CodePosition position, ParsedExpression value, IParsedType type) {
		super(position);
		
		this.value = value;
		this.type = type;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IMethodScope<E, T> environment, T asType) {
		T compiledType = type.compile(environment);
		
		IPartialExpression<E, T> result = value.compile(environment, compiledType)
				.cast(getPosition(), compiledType);
		
		if (asType != null)
			result = result.cast(getPosition(), asType);
		
		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment) {
		// not (yet) possible
		return null;
	}
}
