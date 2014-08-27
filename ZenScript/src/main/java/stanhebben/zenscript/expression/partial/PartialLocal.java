/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression.partial;

import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionLocalGet;
import stanhebben.zenscript.expression.ExpressionLocalSet;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import zenscript.symbolic.unit.SymbolicFunction;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class PartialLocal implements IPartialExpression {
	private final ZenPosition position;
	private final IScopeMethod environment;
	private final SymbolLocal variable;
	
	public PartialLocal(ZenPosition position, IScopeMethod environment, SymbolLocal variable) {
		this.position = position;
		this.environment = environment;
		this.variable = variable;
	}

	@Override
	public Expression eval() {
		return new ExpressionLocalGet(position, environment, variable);
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, String name) {
		return variable.getType().getMember(position, environment, this, name);
	}

	@Override
	public Expression assign(ZenPosition position, Expression other) {
		if (variable.isFinal()) {
			environment.error(position, "value cannot be changed");
			return new ExpressionInvalid(position, environment);
		} else {
			return new ExpressionLocalSet(position, environment, variable, other);
		}
	}

	/*@Override
	public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
		return variable.getType().call(position, environment, eval(environment), values);
	}

	@Override
	public ZenType[] predictCallTypes(int numArguments) {
		return variable.getType().predictCallTypes(numArguments);
	}*/

	@Override
	public IZenSymbol toSymbol() {
		return variable;
	}

	@Override
	public ZenType getType() {
		return variable.getType();
	}

	@Override
	public ZenType toType(List<ZenType> genericTypes) {
		environment.error(position, "not a valid type");
		return environment.getTypes().ANY;
	}

	@Override
	public List<IJavaMethod> getMethods() {
		return variable.getType().getMethods();
	}

	@Override
	public IPartialExpression via(SymbolicFunction function) {
		return function.addCapture(variable);
	}
}
