/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression.partial;

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
	
	public IZenSymbol toSymbol();
	
	public ZenType getType();
	
	public ZenType toType(List<ZenType> genericTypes);
	
	public IPartialExpression via(SymbolicFunction function);
}
