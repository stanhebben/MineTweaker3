/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.member.IGetter;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class MemberStatic implements IPartialExpression
{
	private final CodePosition position;
	private final IScopeMethod scope;
	private final ZenType target;
	
	private final String name;
	
	private IGetter getter;
	private ISetter setter;
	private final List<IMethod> methods;
	
	public MemberStatic(CodePosition position, IScopeMethod scope, ZenType target, String name) {
		this.position = position;
		this.scope = scope;
		
		this.target = target;
		this.name = name;
		this.methods = new ArrayList<IMethod>();
	}
	
	private MemberStatic(CodePosition position, IScopeMethod scope, MemberStatic original) {
		this.position = position;
		this.scope = scope;
		
		this.target = original.target;
		this.name = original.name;
		this.methods = original.methods;
	}
	
	public CodePosition getPosition()
	{
		return position;
	}
	
	public IScopeMethod getScope()
	{
		return scope;
	}
	
	public ZenType getTarget()
	{
		return target;
	}
	
	public String getName()
	{
		return name;
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
	
	public MemberStatic makeVariant(CodePosition position, IScopeMethod scope)
	{
		return new MemberStatic(position, scope, this);
	}

	@Override
	public Expression eval()
	{
		if (getter == null)
			throw new UnsupportedOperationException("This member is not a property or not readable");
		
		return getter.compileGet(position, scope).eval();
	}

	@Override
	public Expression assign(CodePosition position, Expression other)
	{
		if (setter == null)
			throw new UnsupportedOperationException("This member is not a properly or not writeable");
		
		return setter.compile(position, scope, other);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name)
	{
		if (getter == null)
			throw new UnsupportedOperationException("This member is not a property or not readable");
		
		return getter.compileGet(this.position, scope).getMember(position, name);
	}
	
	@Override
	public IPartialExpression call(CodePosition position, IMethod method, Expression[] arguments)
	{
		return method.callStatic(position, scope, arguments);
	}

	@Override
	public List<IMethod> getMethods()
	{
		return methods;
	}

	@Override
	public IZenSymbol toSymbol()
	{
		return new MemberStaticSymbol(this);
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
