/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.test.type;

import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.test.expression.TestExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class TestType implements IZenType<TestExpression, TestType>
{

	@Override
	public IScopeGlobal<TestExpression, TestType> getScope()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ICastingRule<TestExpression, TestType> getCastingRule(AccessScope access, TestType toType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean canCastImplicit(AccessScope access, TestType toType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean canCastExplicit(AccessScope access, TestType toType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IMethod<TestExpression, TestType>> getInstanceMethods()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IMethod<TestExpression, TestType>> getStaticMethods()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IMethod<TestExpression, TestType>> getConstructors()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestType nullable()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestType nonNull()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isNullable()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestExpression call(CodePosition position, IScopeMethod<TestExpression, TestType> scope, IMethod<TestExpression, TestType> method, TestExpression... arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IPartialExpression<TestExpression, TestType> getInstanceMember(CodePosition position, IScopeMethod<TestExpression, TestType> scope, TestExpression instance, String name)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IPartialExpression<TestExpression, TestType> getStaticMember(CodePosition position, IScopeMethod<TestExpression, TestType> scope, String name)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestExpression createDefaultValue(CodePosition position, IScopeMethod<TestExpression, TestType> scope)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestType getArrayBaseType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestType getMapKeyType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestType getMapValueType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<TestType> predictOperatorArgumentType(OperatorType operator)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestExpression unary(CodePosition position, IScopeMethod<TestExpression, TestType> scope, OperatorType operator, TestExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestExpression binary(CodePosition position, IScopeMethod<TestExpression, TestType> scope, OperatorType operator, TestExpression left, TestExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestExpression ternary(CodePosition position, IScopeMethod<TestExpression, TestType> scope, OperatorType operator, TestExpression first, TestExpression second, TestExpression third)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TestExpression compare(CodePosition position, IScopeMethod<TestExpression, TestType> scope, TestExpression left, TestExpression right, CompareType comparator)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public MethodHeader<TestExpression, TestType> getFunctionHeader()
	{
		return null;
	}

	@Override
	public List<TestType> getIteratorTypes(int numArguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
