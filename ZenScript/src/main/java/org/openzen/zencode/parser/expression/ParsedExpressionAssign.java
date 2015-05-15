/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan Hebben
 */
public class ParsedExpressionAssign extends ParsedExpression
{
	private final ParsedExpression left;
	private final ParsedExpression right;

	public ParsedExpressionAssign(CodePosition position, ParsedExpression left, ParsedExpression right)
	{
		super(position);

		this.left = left;
		this.right = right;
	}
	
	// #######################################
	// ### ParsedExpression implementation ###
	// #######################################

	@Override
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, IGenericType<E> asType)
	{
		IPartialExpression<E> cLeft = left.compilePartial(scope, asType);

		E result = cLeft.assign(
				getPosition(),
				right.compile(scope, cLeft.getType()));
		
		if (asType != null)
			result = result.cast(getPosition(), asType);
		
		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		return null;
	}
}
