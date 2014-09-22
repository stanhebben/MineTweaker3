/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.AnyNull;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionNull extends ParsedExpression {
	public ParsedExpressionNull(ZenPosition position) {
		super(position);
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionNull(getPosition(), environment);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return AnyNull.INSTANCE;
	}
}
