/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionDollar extends ParsedExpression {
	private final String name;
	
	public ParsedExpressionDollar(CodePosition position, String name) {
		super(position);
		
		this.name = name;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		IZenSymbol symbol = environment.getEnvironment().getDollar(name);
		if (symbol == null) {
			environment.error(getPosition(), "Dollar variable not found: " + name);
			return new ExpressionInvalid(getPosition(), environment, predictedType);
		} else {
			return symbol.instance(getPosition(), environment);
		}
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		if (name == null)
			return null;
		
		return environment.evalDollar(name);
	}
}
