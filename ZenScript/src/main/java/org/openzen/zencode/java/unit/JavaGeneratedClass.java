/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaGeneratedClass implements IZenType<IJavaExpression, IJavaType>
{
	private final IScopeGlobal<IJavaExpression, IJavaType> scope;
	private final String className;
	private final List<JavaGeneratedField> fields;
	private final List<JavaGeneratedMethod> methods;
	
	public JavaGeneratedClass(IScopeGlobal<IJavaExpression, IJavaType> scope, String className)
	{
		this.scope = scope;
		this.className = className;
		fields = new ArrayList<JavaGeneratedField>();
		methods = new ArrayList<JavaGeneratedMethod>();
	}

	@Override
	public IScopeGlobal<IJavaExpression, IJavaType> getScope()
	{
		return scope;
	}

	@Override
	public ICastingRule<IJavaExpression, IJavaType> getCastingRule(AccessScope access, IJavaType toType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean canCastImplicit(AccessScope access, IJavaType toType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean canCastExplicit(AccessScope access, IJavaType toType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IMethod<IJavaExpression, IJavaType>> getInstanceMethods()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IMethod<IJavaExpression, IJavaType>> getStaticMethods()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IMethod<IJavaExpression, IJavaType>> getConstructors()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType nullable()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType nonNull()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isNullable()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression call(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IMethod<IJavaExpression, IJavaType> method, IJavaExpression... arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IPartialExpression<IJavaExpression, IJavaType> getInstanceMember(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression instance, String name)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IPartialExpression<IJavaExpression, IJavaType> getStaticMember(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, String name)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression createDefaultValue(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getArrayBaseType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getMapKeyType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getMapValueType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IJavaType> predictOperatorArgumentType(OperatorType operator)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression unary(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, OperatorType operator, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression binary(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, OperatorType operator, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ternary(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, OperatorType operator, IJavaExpression first, IJavaExpression second, IJavaExpression third)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compare(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right, CompareType comparator)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public MethodHeader<IJavaExpression, IJavaType> getFunctionHeader()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IJavaType> getIteratorTypes(int numArguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
