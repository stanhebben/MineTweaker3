/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.member.IGetter;
import zenscript.symbolic.member.ISetter;
import zenscript.symbolic.method.IMethod;
import zenscript.symbolic.unit.SymbolicFunction;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class MemberVirtual implements IPartialExpression
{
	private final ZenPosition position;
	private final IScopeMethod scope;
	private final Expression target;
	
	private final boolean isStatic;
	private final String name;
	
	private IGetter getter;
	private ISetter setter;
	private final List<IMethod> methods;
	
	public MemberVirtual(ZenPosition position, IScopeMethod scope, Expression target, String name, boolean isStatic) {
		this.position = position;
		this.scope = scope;
		
		this.target = target;
		this.name = name;
		this.isStatic = isStatic;
		this.methods = new ArrayList<IMethod>();
	}
	
	private MemberVirtual(ZenPosition position, IScopeMethod scope, MemberVirtual original) {
		this.position = position;
		this.scope = scope;
		
		this.target = original.target;
		this.name = original.name;
		this.isStatic = original.isStatic;
		this.methods = original.methods;
	}
	
	public ZenPosition getPosition()
	{
		return position;
	}
	
	public IScopeMethod getScope()
	{
		return scope;
	}
	
	public void setGetter(IGetter getter)
	{
		this.getter = getter;
	}
	
	public void setSetter(ISetter setter)
	{
		this.setter = setter;
	}
	
	public void addMethod(IMethod method)
	{
		methods.add(method);
	}
	
	public boolean isEmpty()
	{
		return getter == null && setter == null && methods.isEmpty();
	}
	
	public MemberVirtual makeVariant(ZenPosition position, IScopeMethod scope)
	{
		return new MemberVirtual(position, scope, this);
	}

	@Override
	public Expression eval()
	{
		if (getter == null)
			throw new UnsupportedOperationException("This member is not a property or not readable");
		
		return getter.compileGet(position, scope).eval();
	}

	@Override
	public Expression assign(ZenPosition position, Expression other)
	{
		if (setter == null)
			throw new UnsupportedOperationException("This member is not a properly or not writeable");
		
		return setter.compile(position, scope, other);
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, String name)
	{
		if (getter == null)
			throw new UnsupportedOperationException("This member is not a property or not readable");
		
		return getter.compileGet(position, scope);
	}

	@Override
	public List<IMethod> getMethods()
	{
		return methods;
	}

	@Override
	public IZenSymbol toSymbol()
	{
		return new MemberVirtualSymbol(this);
	}

	@Override
	public ZenType getType()
	{
		if (getter == null)
			return null;
		
		return getter.getType();
	}

	@Override
	public ZenType toType(List<ZenType> genericTypes)
	{
		return null;
	}

	@Override
	public IPartialExpression via(SymbolicFunction function)
	{
		return this;
	}
}
