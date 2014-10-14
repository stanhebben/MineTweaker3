/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionInvalid extends ParsedExpression {
	public ParsedExpressionInvalid(ZenPosition position) {
		super(position);
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionInvalid(getPosition(), environment, predictedType == null ? environment.getTypes().ANY : predictedType);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
