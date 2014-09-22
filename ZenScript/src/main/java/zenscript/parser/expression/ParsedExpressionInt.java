/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.AnyLong;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionInt extends ParsedExpression {
	private final long value;
	
	public ParsedExpressionInt(ZenPosition position, long value) {
		super(position);
		
		this.value = value;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		Expression result = new ExpressionInt(getPosition(), environment, value, environment.getTypes().INT);
		if (predictedType != null) {
			result = result.cast(getPosition(), predictedType);
		}
		
		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return new AnyLong(value);
	}
}
