/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minetweaker.mc1710.brackets;

import java.util.List;
import minetweaker.annotations.BracketHandler;
import minetweaker.api.IBracketHandler;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.mc1710.oredict.MCOreDictEntry;
import org.openzen.zencode.java.JavaNative;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
@BracketHandler
public class OreBracketHandler implements IBracketHandler
{
	public static IOreDictEntry getOre(String name)
	{
		return new MCOreDictEntry(name);
	}

	private final IJavaMethod method;

	public OreBracketHandler(IModuleScope<IJavaExpression> scope)
	{
		method = JavaNative.getStaticMethod(scope, OreBracketHandler.class, "getOre", String.class);
	}

	@Override
	public IJavaExpression resolve(CodePosition position, IMethodScope<IJavaExpression> scope, List<Token> tokens)
	{
		if (tokens.size() > 2)
			if (tokens.get(0).getValue().equals("ore") && tokens.get(1).getValue().equals(":"))
				return find(position, scope, tokens, 2, tokens.size());

		return null;
	}

	@Override
	public IAny eval(List<Token> tokens)
	{
		return null;
	}

	private IJavaExpression find(CodePosition position, IMethodScope<IJavaExpression> scope, List<Token> tokens, int startIndex, int endIndex)
	{
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			Token token = tokens.get(i);
			valueBuilder.append(token.getValue());
		}
		
		return method.callStaticWithConstants(position, scope, valueBuilder.toString());
	}
}
