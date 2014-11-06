/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionIndex extends ParsedExpression {
	private final ParsedExpression value;
	private final ParsedExpression index;
	
	public ParsedExpressionIndex(CodePosition position, ParsedExpression value, ParsedExpression index) {
		super(position);
		
		this.value = value;
		this.index = index;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		// TODO: improve type prediction for this
		Expression cValue = value.compile(environment, null);
		Expression cIndex = index.compile(environment, null);
		return cValue.getType().operator(getPosition(), environment, OperatorType.INDEXGET, cValue, cIndex);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny valueValue = value.eval(environment);
		if (valueValue == null)
			return null;
		
		IAny indexValue = index.eval(environment);
		if (indexValue == null)
			return null;
		
		return valueValue.indexGet(indexValue);
	}
}
