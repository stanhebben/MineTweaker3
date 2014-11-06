/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.symbols;

import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.expression.partial.PartialType;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class SymbolType implements IZenSymbol {
	private final ZenType type;
	
	public SymbolType(ZenType type) {
		this.type = type;
	}

	@Override
	public IPartialExpression instance(CodePosition position, IScopeMethod environment) {
		return new PartialType(position, environment, type);
	}
}
