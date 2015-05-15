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
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionConditional extends ParsedExpression
{
	private final ParsedExpression condition;
	private final ParsedExpression ifThen;
	private final ParsedExpression ifElse;

	public ParsedExpressionConditional(CodePosition position, ParsedExpression condition, ParsedExpression ifThen, ParsedExpression ifElse)
	{
		super(position);

		this.condition = condition;
		this.ifThen = ifThen;
		this.ifElse = ifElse;
	}

	@Override
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, IGenericType<E> asType)
	{
		E result = scope.getExpressionCompiler().ternary(
				getPosition(),
				scope,
				condition.compile(scope, scope.getTypeCompiler().bool),
				ifThen.compile(scope, asType),
				ifElse.compile(scope, asType));
		
		if (asType != null)
			result = result.cast(getPosition(), asType);
		
		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		IAny conditionValue = condition.eval(environment);
		if (conditionValue == null)
			return null;

		boolean conditionBool = conditionValue.asBool();
		if (conditionBool)
			return ifThen.eval(environment);
		else
			return ifElse.eval(environment);
	}
}
