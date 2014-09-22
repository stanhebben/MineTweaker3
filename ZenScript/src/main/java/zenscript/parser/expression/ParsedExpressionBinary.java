/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.AnyBool;
import zenscript.runtime.AnyInt;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionBinary extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	private final OperatorType operator;
	
	public ParsedExpressionBinary(ZenPosition position, ParsedExpression left, ParsedExpression right, OperatorType operator) {
		super(position);
		
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		// TODO: make better predictions
		Expression cLeft = left.compile(environment, predictedType).eval();
		Expression cRight = right.compile(environment, predictedType).eval();
		return cLeft.getType().binary(getPosition(), environment, cLeft, cRight, operator);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny leftValue = left.eval(environment);
		IAny rightValue = right.eval(environment);
		
		switch (operator) {
			case ADD:
				return leftValue.add(rightValue);
			case SUB:
				return leftValue.sub(rightValue);
			case MUL:
				return leftValue.mul(rightValue);
			case DIV:
				return leftValue.div(rightValue);
			case MOD:
				return leftValue.mod(rightValue);
			case CAT:
				return leftValue.cat(rightValue);
			case OR:
				return leftValue.or(rightValue);
			case AND:
				return leftValue.and(rightValue);
			case XOR:
				return leftValue.xor(rightValue);
			case INDEXGET:
				return leftValue.indexGet(rightValue);
			case RANGE:
				return leftValue.range(rightValue);
			case CONTAINS:
				return AnyBool.valueOf(leftValue.contains(rightValue));
			case COMPARE:
				return new AnyInt(leftValue.compareTo(rightValue));
			case MEMBERGETTER:
				return leftValue.memberGet(rightValue.asString());
			case EQUALS:
				return AnyBool.valueOf(leftValue.equals(rightValue));
			default:
				throw new AssertionError("Invalid operation type");
		}
	}
}
