/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionFloat;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.AnyDouble;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionFloat extends ParsedExpression {
	private final double value;
	
	public ParsedExpressionFloat(ZenPosition position, double value) {
		super(position);
		
		this.value = value;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionFloat(
				getPosition(),
				environment,
				value,
				predictedType == environment.getTypes().FLOAT ? predictedType : environment.getTypes().DOUBLE);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return new AnyDouble(value);
	}
}
