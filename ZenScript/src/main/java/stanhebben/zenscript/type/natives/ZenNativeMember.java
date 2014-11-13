/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import org.openzen.zencode.java.method.JavaMethod;
import org.openzen.zencode.symbolic.method.IMethod;
import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.ExpressionInvalid;

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

	public IPartialExpression instance(CodePosition position, IScopeMethod environment, IPartialExpression value) {
		return new InstanceGetValue(position, environment, value);
	}

	public IPartialExpression instance(CodePosition position, IScopeMethod environment) {
		return new StaticGetValue(position, environment);
	}
	
	public void addMethod(JavaMethod method) {
		methods.add(method);
	}
	
	private class InstanceGetValue implements IPartialExpression {
		private final CodePosition position;
		private final IScopeMethod scope;
		private final IPartialExpression value;
		
		public InstanceGetValue(CodePosition position, IScopeMethod scope, IPartialExpression value) {
			this.position = position;
			this.scope = scope;
			this.value = value;
		}
		
		@Override
		public Expression eval() {
			if (getter == null) {
				scope.error(position, "This member doesn't have a getter");
				return new ExpressionInvalid(position, scope);
			}
			
			return new ExpressionCallVirtual(position, scope, getter, value.eval());
		}

		@Override
		public Expression assign(CodePosition position, Expression other) {
			return new ExpressionCallVirtual(position, scope, setter, value.eval(), other);
		}

		@Override
		public IPartialExpression getMember(CodePosition position, String name) {
			return getter.getReturnType().getMember(position, scope, this, name);
		}
		
		@Override
		public IPartialExpression call(CodePosition position, IMethod method, Expression[] arguments)
		{
			return new ExpressionCallVirtual(position, scope, method, value.eval(), arguments);
		}

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
			scope.error(position, "not a valid type");
			return scope.getTypes().ANY;
		}
		
		@Override
		public List<IMethod> getMethods() {
			if (getter == null)
				return methods;
			
			return getter.getReturnType().getMethods();
		}

		@Override
		public IPartialExpression via(SymbolicFunction function)
		{
			return this;
		}
	}
	
	private class StaticGetValue implements IPartialExpression
	{
		private final CodePosition position;
		private final IScopeMethod scope;
		
		public StaticGetValue(CodePosition position, IScopeMethod scope) {
			this.position = position;
			this.scope = scope;
		}
		
		@Override
		public Expression eval() {
			return new ExpressionCallStatic(position, scope, setter);
		}

		@Override
		public Expression assign(CodePosition position, Expression other) {
			return new ExpressionCallStatic(position, scope, setter);
		}

		@Override
		public IPartialExpression getMember(CodePosition position, String name) {
			return getter.getReturnType().getMember(position, scope, this, name);
		}
		
		@Override
		public IPartialExpression call(CodePosition position, IMethod method, Expression[] arguments)
		{
			return method.callStatic(position, scope, arguments);
		}

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
			scope.error(position, "not a valid type");
			return scope.getTypes().ANY;
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
		public IPartialExpression instance(CodePosition position, IScopeMethod environment) {
			return new StaticGetValue(position, environment);
		}
	}
}
