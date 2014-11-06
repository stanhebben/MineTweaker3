/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression.partial;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolJavaStaticMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class PartialStaticMethod implements IPartialExpression {
	private final CodePosition position;
	private final IScopeMethod environment;
	private final IMethod method;
	
	public PartialStaticMethod(CodePosition position, IScopeMethod environment, IMethod method) {
		this.position = position;
		this.environment = environment;
		this.method = method;
	}

	@Override
	public Expression eval() {
		environment.error(position, "not a valid expression");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public Expression assign(CodePosition position, Expression other) {
		environment.error(position, "cannot alter this final");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name) {
		environment.error(position, "methods have no members");
		return new ExpressionInvalid(position, environment);
	}

	/*@Override
	public Expression call(CodePosition position, IEnvironmentMethod environment, Expression... values) {
		if (method.accepts(environment, values)) {
			return new ExpressionCallStatic(position, environment, method, values);
		} else {
			environment.error(position, "parameter count mismatch: got " + values.length + " arguments");
			return new ExpressionInvalid(position, method.getReturnType());
		}
	}

	@Override
	public ZenType[] predictCallTypes(int numArguments) {
		return Arrays.copyOf(method.getParameterTypes(), numArguments);
	}*/

	@Override
	public IZenSymbol toSymbol() {
		return new SymbolJavaStaticMethod(method);
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
		return Collections.singletonList(method);
	}

	@Override
	public IPartialExpression via(SymbolicFunction function) {
		return this;
	}
}
