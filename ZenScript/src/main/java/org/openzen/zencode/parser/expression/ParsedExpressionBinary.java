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
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.AnyInt;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionBinary extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	private final OperatorType operator;
	
	public ParsedExpressionBinary(CodePosition position, ParsedExpression left, ParsedExpression right, OperatorType operator) {
		super(position);
		
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		// TODO: make better predictions
		Expression cLeft = left.compile(environment, predictedType);
		Expression cRight = right.compile(environment, predictedType);
		return cLeft.getType().operator(getPosition(), environment, operator, cLeft, cRight);
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
