/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression.partial;

import java.util.Collections;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolType;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class PartialType implements IPartialExpression {
	private final ZenPosition position;
	private final IScopeMethod environment;
	private final ZenType type;
	
	public PartialType(ZenPosition position, IScopeMethod environment, ZenType type) {
		this.position = position;
		this.environment = environment;
		this.type = type;
	}

	@Override
	public Expression eval() {
		environment.error(position, "cannot use type as expression");
		return new ExpressionInvalid(position, environment, type);
	}

	@Override
	public Expression assign(ZenPosition position, Expression other) {
		environment.error(position, "cannot assign to a type");
		return new ExpressionInvalid(position, environment, type);
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, String name) {
		return type.getStaticMember(position, environment, name);
	}

	/*@Override
	public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
		environment.error(position, "cannot call a type");
		return new ExpressionInvalid(position, type);
	}*/

	@Override
	public IZenSymbol toSymbol() {
		return new SymbolType(type);
	}
	
	@Override
	public ZenType getType() {
		return null; // not an expression
	}

	/*@Override
	public ZenType[] predictCallTypes(int numArguments) {
		return new ZenType[numArguments];
	}*/

	@Override
	public ZenType toType(List<ZenType> genericTypes) {
		return type;
	}

	@Override
	public List<IJavaMethod> getMethods() {
		return Collections.EMPTY_LIST;
	}
}
