/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression.partial;

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
	private final IScopeMethod environment;
	private final IMethod method;
	
	public PartialStaticGetter(CodePosition position, IScopeMethod environment, IMethod method) {
		this.position = position;
		this.environment = environment;
		this.method = method;
	}

	@Override
	public Expression eval() {
		return new ExpressionCallStatic(position, environment, method);
	}

	@Override
	public Expression assign(CodePosition position, Expression other) {
		environment.error(position, "cannot alter this final");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name) {
		return method.getReturnType().getMember(position, environment, this, name);
	}

	/*@Override
	public Expression call(CodePosition position, IEnvironmentMethod environment, Expression... values) {
		environment.error(position, "value cannot be called");
		return new ExpressionInvalid(position);
	}

	@Override
	public ZenType[] predictCallTypes(int numArguments) {
		return new ZenType[numArguments];
	}*/

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
		environment.error(position, "not a valid type");
		return environment.getTypes().ANY;
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
