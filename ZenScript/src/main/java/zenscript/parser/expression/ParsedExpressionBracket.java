/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import java.util.List;
import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;
import zenscript.lexer.Token;
import zenscript.runtime.IAny;

/**
 *
 * @author Stan
 */
public class ParsedExpressionBracket extends ParsedExpression {
	private final List<Token> tokens;
	
	public ParsedExpressionBracket(ZenPosition position, List<Token> tokens) {
		super(position);
		
		this.tokens = tokens;
	}

	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
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
