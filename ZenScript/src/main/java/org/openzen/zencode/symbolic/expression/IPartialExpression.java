/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.expression;

import java.util.List;
import stanhebben.zenscript.expression.Expression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan Hebben
 */
public interface IPartialExpression
{
	public Expression eval();
	
	public Expression assign(CodePosition position, Expression other);
	
	public IPartialExpression getMember(CodePosition position, String name);
	
	public List<IMethod> getMethods();
	
	/**
	 * Calls the given method from this expression. Method must be one of the
	 * methods offered by this expression, and arguments must match method parameters
	 * exactly.
	 * 
	 * @param position
	 * @param method
	 * @param arguments
	 * @return 
	 */
	public IPartialExpression call(CodePosition position, IMethod method, Expression[] arguments);
	
	public IZenSymbol toSymbol();
	
	public ZenType getType();
	
	public ZenType toType(List<ZenType> genericTypes);
	
	public IPartialExpression via(SymbolicFunction function);
}
