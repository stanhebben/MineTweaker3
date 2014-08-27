/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression.partial;

import java.util.List;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import zenscript.symbolic.unit.SymbolicFunction;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public interface IPartialExpression {
	public Expression eval();
	
	public Expression assign(ZenPosition position, Expression other);
	
	public IPartialExpression getMember(ZenPosition position, String name);
	
	public List<IJavaMethod> getMethods();
	
	public IZenSymbol toSymbol();
	
	public ZenType getType();
	
	public ZenType toType(List<ZenType> genericTypes);
	
	public IPartialExpression via(SymbolicFunction function);
}
