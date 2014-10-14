/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionBool;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.AnyBool;
import zenscript.runtime.IAny;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionBool extends ParsedExpression {
	private final boolean value;
	
	public ParsedExpressionBool(ZenPosition position, boolean value) {
		super(position);
		
		this.value = value;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionBool(getPosition(), environment, value);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return AnyBool.valueOf(value);
	}
}
