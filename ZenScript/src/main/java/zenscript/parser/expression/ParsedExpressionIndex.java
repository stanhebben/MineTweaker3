/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionIndex extends ParsedExpression {
	private final ParsedExpression value;
	private final ParsedExpression index;
	
	public ParsedExpressionIndex(ZenPosition position, ParsedExpression value, ParsedExpression index) {
		super(position);
		
		this.value = value;
		this.index = index;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		// TODO: improve type prediction for this
		Expression cValue = value.compile(environment, null).eval();
		Expression cIndex = index.compile(environment, null).eval();
		return cValue.getType().binary(getPosition(), environment, cValue, cIndex, OperatorType.INDEXGET);
	}
}
