/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression.partial;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionInvalid;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolJavaStaticGetter;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class PartialStaticGetter implements IPartialExpression {
	private final CodePosition position;
	private final IScopeMethod scope;
	private final IMethod method;
	
	public PartialStaticGetter(CodePosition position, IScopeMethod scope, IMethod method) {
		this.position = position;
		this.scope = scope;
		this.method = method;
	}

	@Override
	public Expression eval() {
		return new ExpressionCallStatic(position, scope, method);
	}

	@Override
	public Expression assign(CodePosition position, Expression other) {
		scope.error(position, "cannot alter this final");
		return new ExpressionInvalid(position, scope);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name) {
		return method.getReturnType().getMember(position, scope, this, name);
	}

	@Override
	public IPartialExpression call(CodePosition position, IMethod method, Expression[] arguments) {
		return method.callVirtual(position, scope, this.method.callStatic(position, scope), arguments);
	}

	@Override
	public IZenSymbol toSymbol() {
		return new SymbolJavaStaticGetter(method);
	}

	@Override
	public ZenType getType() {
		return method.getReturnType();
	}

	@Override
	public ZenType toType(List<ZenType> genericTypes) {
		scope.error(position, "not a valid type");
		return scope.getTypes().ANY;
	}

	@Override
	public List<IMethod> getMethods() {
		return method.getReturnType().getMethods();
	}

	@Override
	public IPartialExpression via(SymbolicFunction function) {
		return this;
	}
}
