/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionOrOr;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionOrOr extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	
	public ParsedExpressionOrOr(ZenPosition position, ParsedExpression left, ParsedExpression right) {
		super(position);
		
		this.left = left;
		this.right = right;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		Expression cLeft = left.compile(environment, predictedType).eval();
		Expression cRight = right.compile(environment, predictedType).eval();
		
		ZenType type;
		if (cRight.getType().canCastImplicit(cLeft.getType())) {
			type = cLeft.getType();
		} else if (cLeft.getType().canCastImplicit(cRight.getType())) {
			type = cRight.getType();
		} else {
			environment.error(getPosition(), "These types could not be unified: " + cLeft.getType() + " and " + cRight.getType());
			type = environment.getTypes().ANY;
		}
		
		return new ExpressionOrOr(
				getPosition(),
				environment,
				cLeft.cast(getPosition(), type),
				cRight.cast(getPosition(), type));
	}
}
