/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.mc1710.brackets;

import java.util.List;
import minetweaker.IBracketHandler;
import minetweaker.annotations.BracketHandler;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.mc1710.oredict.MCOreDictEntry;
import minetweaker.runtime.symbol.SymbolUtil;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.lexer.Token;
import zenscript.runtime.IAny;
import zenscript.symbolic.method.IMethod;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
@BracketHandler
public class OreBracketHandler implements IBracketHandler {
	public static IOreDictEntry getOre(String name) {
		return new MCOreDictEntry(name);
	}
	
	private final IMethod method;
	
	public OreBracketHandler(IScopeGlobal scope) {
		method = SymbolUtil.getZenStaticMethod(scope, OreBracketHandler.class, "getOre", String.class);
	}
	
	@Override
	public IZenSymbol resolve(List<Token> tokens) {
		if (tokens.size() > 2) {
			if (tokens.get(0).getValue().equals("ore") && tokens.get(1).getValue().equals(":")) {
				return find(tokens, 2, tokens.size());
			}
		}
		
		return null;
	}
	
	@Override
	public IAny eval(List<Token> tokens) {
		return null;
	}
	
	private IZenSymbol find(List<Token> tokens, int startIndex, int endIndex) {
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			Token token = tokens.get(i);
			valueBuilder.append(token.getValue());
		}
		
		return new OreReferenceSymbol(valueBuilder.toString());
	}
	
	private class OreReferenceSymbol implements IZenSymbol {
		private final String name;
		
		public OreReferenceSymbol(String name) {
			this.name = name;
		}
		
		@Override
		public IPartialExpression instance(ZenPosition position, IScopeMethod scope) {
			return new ExpressionCallStatic(
					position,
					scope,
					method,
					new ExpressionString(position, scope, name));
		}
	}
}
