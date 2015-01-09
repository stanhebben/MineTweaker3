/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyRange;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 */
public class ParsedExpressionRange extends ParsedExpression
{
	private final ParsedExpression from;
	private final ParsedExpression to;

	public ParsedExpressionRange(ParsedExpression from, ParsedExpression to)
	{
		super(from.getPosition());

		this.from = from;
		this.to = to;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IMethodScope<E, T> scope, T predictedType)
	{
		E compiledFrom = from.compile(scope, null);
		E compiledTo = to.compile(scope, null);
		return scope.getExpressionCompiler().range(getPosition(), scope, compiledFrom, compiledTo);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		IAny fromValue = from.eval(environment);
		if (fromValue == null)
			return null;

		IAny toValue = to.eval(environment);
		if (toValue == null)
			return null;

		int iFrom = fromValue.asInt();
		int iTo = toValue.asInt();
		return new AnyRange(iFrom, iTo);
	}
}
