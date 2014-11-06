/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan Hebben
 */
public interface IZenSymbol
{
	public IPartialExpression instance(CodePosition position, IScopeMethod scope);
}
