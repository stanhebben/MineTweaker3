/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import zenscript.annotations.CompareType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.AnyBool;
import zenscript.runtime.IAny;
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

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny leftValue = left.eval(environment);
		if (leftValue == null)
			return null;
		
		IAny rightValue = right.eval(environment);
		if (rightValue == null)
			return null;
		
		int result = leftValue.compareTo(rightValue);
		switch (type) {
			case LT: return AnyBool.valueOf(result < 0);
			case GT: return AnyBool.valueOf(result > 0);
			case EQ: return AnyBool.valueOf(result == 0);
			case NE: return AnyBool.valueOf(result != 0);
			case LE: return AnyBool.valueOf(result <= 0);
			case GE: return AnyBool.valueOf(result >= 0);
			default:
				throw new AssertionError("Illegal compare type");
		}
	}
}
