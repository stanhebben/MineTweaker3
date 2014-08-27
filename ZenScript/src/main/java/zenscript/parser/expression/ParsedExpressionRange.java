/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionIntegerRange;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public class ParsedExpressionRange extends ParsedExpression {
	private final ParsedExpression from;
	private final ParsedExpression to;
	
	public ParsedExpressionRange(ParsedExpression from, ParsedExpression to) {
		super(from.getPosition());
		
		this.from = from;
		this.to = to;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		Expression compiledFrom = from.compile(environment, environment.getTypes().INT).eval();
		Expression compiledTo = to.compile(environment, environment.getTypes().INT).eval();
		return new ExpressionIntegerRange(getPosition(), environment, compiledFrom, compiledTo);
	}
}
