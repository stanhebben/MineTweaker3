/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import zenscript.annotations.CompareType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionCompare extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	private final CompareType type;
	
	public ParsedExpressionCompare(
			ZenPosition position,
			ParsedExpression left,
			ParsedExpression right,
			CompareType type) {
		super(position);
		
		this.left = left;
		this.right = right;
		this.type = type;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		Expression cLeft = left.compile(environment, null).eval();
		Expression cRight = right.compile(environment, null).eval();
		
		return cLeft.getType().compare(getPosition(), environment, cLeft, cRight, type)
				.cast(getPosition(), predictedType);
	}
}
