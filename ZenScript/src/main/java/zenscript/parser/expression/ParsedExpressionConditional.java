/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionConditional;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionConditional extends ParsedExpression {
	private final ParsedExpression condition;
	private final ParsedExpression ifThen;
	private final ParsedExpression ifElse;
	
	public ParsedExpressionConditional(ZenPosition position, ParsedExpression condition, ParsedExpression ifThen, ParsedExpression ifElse) {
		super(position);
		
		this.condition = condition;
		this.ifThen = ifThen;
		this.ifElse = ifElse;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionConditional(
				getPosition(),
				environment,
				condition.compile(environment, environment.getTypes().BOOL).eval().cast(getPosition(), environment.getTypes().BOOL),
				ifThen.compile(environment, predictedType).eval(),
				ifElse.compile(environment, predictedType).eval());
	}
}
