/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.symbols;

import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.expression.partial.PartialStaticMethod;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class SymbolJavaStaticMethod implements IZenSymbol {
	private final IMethod method;
	
	public SymbolJavaStaticMethod(IMethod method) {
		this.method = method;
	}

	@Override
	public IPartialExpression instance(CodePosition position, IScopeMethod environment) {
		return new PartialStaticMethod(position, environment, method);
	}
}
