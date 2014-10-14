/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionIntegerRange;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.AnyRange;
import zenscript.runtime.IAny;

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
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		Expression compiledFrom = from.compile(environment, environment.getTypes().INT);
		Expression compiledTo = to.compile(environment, environment.getTypes().INT);
		return new ExpressionIntegerRange(getPosition(), environment, compiledFrom, compiledTo);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny fromValue = from.eval(environment);
		if (fromValue == null)
			return null;
		
		IAny toValue = to.eval(environment);
		if (toValue == null)
			return null;
		
		int iFrom = fromValue.asInt();
		int iTo = toValue.asInt();
		return new AnyRange(iFrom, iTo);
	}
}
