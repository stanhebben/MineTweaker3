/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import zenscript.symbolic.method.JavaMethod;
import zenscript.symbolic.method.IMethod;
import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.unit.SymbolicFunction;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ZenNativeMember {
	private IMethod getter;
	private IMethod setter;
	private final List<IMethod> methods = new ArrayList<IMethod>();
	
	public IMethod getGetter() {
		return getter;
	}
	
	public IMethod getSetter() {
		return setter;
	}
	
	public void setGetter(IMethod getter) {
		if (this.getter == null) {
			this.getter = getter;
		} else {
			throw new RuntimeException("already has a getter");
		}
	}
	
	public void setSetter(IMethod setter) {
		if (this.setter == null) {
			this.setter = setter;
		} else {
			throw new RuntimeException("already has a setter");
		}
	}

	public IPartialExpression instance(ZenPosition position, IScopeMethod environment, IPartialExpression value) {
		return new InstanceGetValue(position, environment, value);
	}

	public IPartialExpression instance(ZenPosition position, IScopeMethod environment) {
		return new StaticGetValue(position, environment);
	}
	
	public void addMethod(JavaMethod method) {
		methods.add(method);
	}
	
	private class InstanceGetValue implements IPartialExpression {
		private final ZenPosition position;
		private final IScopeMethod environment;
		private final IPartialExpression value;
		
		public InstanceGetValue(ZenPosition position, IScopeMethod environment, IPartialExpression value) {
			this.position = position;
			this.environment = environment;
			this.value = value;
		}
		
		@Override
		public Expression eval() {
			return new ExpressionCallVirtual(position, environment, getter, value.eval());
		}

		@Override
		public Expression assign(ZenPosition position, Expression other) {
			return new ExpressionCallVirtual(position, environment, setter, value.eval(), other);
		}

		@Override
		public IPartialExpression getMember(ZenPosition position, String name) {
			return getter.getReturnType().getMember(position, environment, this, name);
		}

		/*@Override
		public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
			IMethod method = JavaMethod.select(false, methods, environment, values);
			if (method == null) {
				environment.error(position, methodMatchingError(methods, values));
				return new ExpressionInvalid(position);
			} else {
				return new ExpressionCallVirtual(position, environment, method, value.eval(environment), values);
			}
		}
		
		@Override
		public ZenType[] predictCallTypes(int numArguments) {
			return JavaMethod.predict(methods, numArguments);
		}*/
		
		@Override
		public IZenSymbol toSymbol() {
			return null;
		}
		
		@Override
		public ZenType getType() {
			return getter.getReturnType();
		}

		@Override
		public ZenType toType(List<ZenType> genericTypes) {
			environment.error(position, "not a valid type");
			return environment.getTypes().ANY;
		}
		
		@Override
		public List<IMethod> getMethods() {
			return getter.getReturnType().getMethods();
		}

		@Override
		public IPartialExpression via(SymbolicFunction function)
		{
			return this;
		}
	}
	
	private class StaticGetValue implements IPartialExpression {
		private final ZenPosition position;
		private final IScopeMethod environment;
		
		public StaticGetValue(ZenPosition position, IScopeMethod environment) {
			this.position = position;
			this.environment = environment;
		}
		
		@Override
		public Expression eval() {
			return new ExpressionCallStatic(position, environment, setter);
		}

		@Override
		public Expression assign(ZenPosition position, Expression other) {
			return new ExpressionCallStatic(position, environment, setter);
		}

		@Override
		public IPartialExpression getMember(ZenPosition position, String name) {
			return getter.getReturnType().getMember(position, environment, this, name);
		}

		/*@Override
		public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
			IMethod method = JavaMethod.select(true, methods, environment, values);
			if (method == null) {
				environment.error(position, methodMatchingError(methods, values));
				return new ExpressionInvalid(position);
			} else {
				return new ExpressionCallStatic(position, environment, method, values);
			}
		}
		
		@Override
		public ZenType[] predictCallTypes(int numArguments) {
			return JavaMethod.predict(methods, numArguments);
		}*/

		@Override
		public IZenSymbol toSymbol() {
			return new StaticSymbol();
		}
		
		@Override
		public ZenType getType() {
			return getter.getReturnType();
		}
		
		@Override
		public ZenType toType(List<ZenType> genericTypes) {
			environment.error(position, "not a valid type");
			return environment.getTypes().ANY;
		}
		
		@Override
		public List<IMethod> getMethods() {
			return getter.getReturnType().getMethods();
		}

		@Override
		public IPartialExpression via(SymbolicFunction function)
		{
			return this;
		}
	}
	
	private class StaticSymbol implements IZenSymbol {
		@Override
		public IPartialExpression instance(ZenPosition position, IScopeMethod environment) {
			return new StaticGetValue(position, environment);
		}
	}
}
