/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionLocalGet;
import stanhebben.zenscript.expression.ExpressionLocalSet;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class PartialLocal implements IPartialExpression
{
	private final CodePosition position;
	private final IScopeMethod scope;
	private final SymbolLocal variable;

	public PartialLocal(CodePosition position, IScopeMethod environment, SymbolLocal variable)
	{
		this.position = position;
		this.scope = environment;
		this.variable = variable;
	}

	@Override
	public Expression eval()
	{
		return new ExpressionLocalGet(position, scope, variable);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name)
	{
		return variable.getType().getMember(position, scope, this, name);
	}

	@Override
	public Expression assign(CodePosition position, Expression other)
	{
		if (variable.isFinal()) {
			scope.error(position, "value cannot be changed");
			return new ExpressionInvalid(position, scope);
		} else
			return new ExpressionLocalSet(position, scope, variable, other);
	}

	@Override
	public IZenSymbol toSymbol()
	{
		return variable;
	}

	@Override
	public ZenType getType()
	{
		return variable.getType();
	}

	@Override
	public ZenType toType(List<ZenType> genericTypes)
	{
		scope.error(position, "not a valid type");
		return scope.getTypes().ANY;
	}

	@Override
	public List<IMethod> getMethods()
	{
		return variable.getType().getMethods();
	}
	
	@Override
	public Expression call(CodePosition position, IMethod method, Expression... arguments)
	{
		return method.callVirtual(position, scope, eval(), arguments);
	}

	@Override
	public IPartialExpression via(SymbolicFunction function)
	{
		return function.addCapture(position, scope, variable);
	}
}
