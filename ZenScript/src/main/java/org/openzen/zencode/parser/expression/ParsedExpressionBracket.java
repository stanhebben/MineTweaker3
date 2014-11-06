/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import java.util.List;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;

/**
 *
 * @author Stan
 */
public class ParsedExpressionBracket extends ParsedExpression {
	private final List<Token> tokens;
	
	public ParsedExpressionBracket(CodePosition position, List<Token> tokens) {
		super(position);
		
		this.tokens = tokens;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		IZenSymbol resolved = environment.getEnvironment().getBracketed(environment, tokens);
		if (resolved == null) {
			StringBuilder builder = new StringBuilder();
			builder.append('<');
			Token last = null;
			for (Token token : tokens) {
				if (last != null) builder.append(' ');
				builder.append(token.getValue());
				last = token;
			}
			builder.append('>');

			environment.error(getPosition(), "Could not resolve " + builder.toString());
			return new ExpressionInvalid(getPosition(), environment);
		} else {
			return resolved.instance(getPosition(), environment);
		}
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return environment.evalBracketed(tokens);
	}
}
