/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.partial.PartialIndexed;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionIndex extends ParsedExpression
{
	private final ParsedExpression value;
	private final ParsedExpression index;

	public ParsedExpressionIndex(CodePosition position, ParsedExpression value, ParsedExpression index)
	{
		super(position);

		this.value = value;
		this.index = index;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, TypeInstance<E> asType)
	{
		E cValue = value.compile(scope, null);
		TypeInstance<E> asKeyType = cValue.getType().predictOperatorArgumentType(OperatorType.INDEXGET).get(0);
		E cIndex = index.compile(scope, asKeyType);
		
		return new PartialIndexed<E>(getPosition(), scope, cValue, cIndex, asType);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		IAny valueValue = value.eval(environment);
		if (valueValue == null)
			return null;

		IAny indexValue = index.eval(environment);
		if (indexValue == null)
			return null;

		return valueValue.indexGet(indexValue);
	}
}
