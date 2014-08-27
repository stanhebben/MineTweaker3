/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionDollar extends ParsedExpression {
	private final String name;
	
	public ParsedExpressionDollar(ZenPosition position, String name) {
		super(position);
		
		this.name = name;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		IZenSymbol symbol = environment.getEnvironment().getDollar(name);
		if (symbol == null) {
			environment.error(getPosition(), "Dollar variable not found: " + name);
			return new ExpressionInvalid(getPosition(), environment, predictedType);
		} else {
			return symbol.instance(getPosition(), environment);
		}
	}
}
