/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.expand;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionCallVirtual;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.method.IMethod;
import stanhebben.zenscript.type.natives.JavaMethodExpanding;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ZenExpandMember {
	private final ZenType type;
	private final String name;
	private IMethod getter;
	private IMethod setter;
	private final List<IMethod> staticMethods = new ArrayList<IMethod>();
	private final List<IMethod> virtualMethods = new ArrayList<IMethod>();
	
	public ZenExpandMember(ZenType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public IPartialExpression instance(ZenPosition position, IScopeMethod environment, IPartialExpression value) {
		return new InstanceGetValue(position, environment, value);
	}
	
	public IPartialExpression instance(ZenPosition position, IScopeMethod environment) {
		return new StaticGetValue(position, environment);
	}
	
	public void setGetter(IMethod getter) {
		if (this.getter != null) {
			throw new RuntimeException(type + "." + name + " already has a getter");
		} else {
			this.getter = getter;
		}
	}
	
	public void setSetter(IMethod setter) {
		if (this.setter != null) {
			throw new RuntimeException(type + "." + name + " already has a setter");
		} else {
			this.setter = setter;
		}
	}
	
	public void addVirtualMethod(IMethod method) {
		virtualMethods.add(new JavaMethodExpanding(type, method));
	}
	
	public void addStaticMethod(IMethod method) {
		staticMethods.add(method);
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
		
		@Override
		public List<IMethod> getMethods() {
			return virtualMethods;
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
			environment.error(position, "not a valid type");
			return environment.getTypes().ANY;
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
			return new ExpressionCallStatic(position, environment, getter);
		}

		@Override
		public Expression assign(ZenPosition position, Expression other) {
			return new ExpressionCallStatic(position, environment, setter, other);
		}

		@Override
		public IPartialExpression getMember(ZenPosition position, String name) {
			return getter.getReturnType().getMember(position, environment, this, name);
		}
		
		@Override
		public List<IMethod> getMethods() {
			return staticMethods;
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
			environment.error(position, "not a valid type");
			return environment.getTypes().ANY;
		}
	}
	
	private class StaticSymbol implements IZenSymbol {
		@Override
		public IPartialExpression instance(ZenPosition position, IScopeMethod environment) {
			return new StaticGetValue(position, environment);
		}
	}
}
