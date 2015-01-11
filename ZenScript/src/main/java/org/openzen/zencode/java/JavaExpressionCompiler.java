/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java;

import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 * Constructs a java expression compiler.
 * 
 * @author Stan
 */
public class JavaExpressionCompiler implements IExpressionCompiler<IJavaExpression>
{
	public JavaExpressionCompiler()
	{
		
	}

	@Override
	public IJavaExpression invalid(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invalid(CodePosition position, IMethodScope<IJavaExpression> scope, TypeInstance<IJavaExpression> valueType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantNull(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantBool(CodePosition position, IMethodScope<IJavaExpression> scope, boolean value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantByte(CodePosition position, IMethodScope<IJavaExpression> scope, byte value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantUByte(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantShort(CodePosition position, IMethodScope<IJavaExpression> scope, short value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantUShort(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantInt(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantUInt(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantLong(CodePosition position, IMethodScope<IJavaExpression> scope, long value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantULong(CodePosition position, IMethodScope<IJavaExpression> scope, long value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantFloat(CodePosition position, IMethodScope<IJavaExpression> scope, float value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantDouble(CodePosition position, IMethodScope<IJavaExpression> scope, double value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantChar(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantString(CodePosition position, IMethodScope<IJavaExpression> scope, String value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constant(CodePosition position, IMethodScope<IJavaExpression> scope, Object value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<IJavaExpression> constants(CodePosition position, IMethodScope<IJavaExpression> scope, Object[] values)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression localGet(CodePosition position, IMethodScope<IJavaExpression> scope, LocalSymbol<IJavaExpression> local)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression localSet(CodePosition position, IMethodScope<IJavaExpression> scope, LocalSymbol<IJavaExpression> local, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andAnd(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orOr(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareGeneric(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value, CompareType comparator)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ternary(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression condition, IJavaExpression ifValue, IJavaExpression elseValue)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression notNull(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression range(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression from, IJavaExpression to)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression array(CodePosition position, IMethodScope<IJavaExpression> scope, TypeInstance<IJavaExpression> arrayType, List<IJavaExpression> values)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression map(CodePosition position, IMethodScope<IJavaExpression> scope, TypeInstance<IJavaExpression> mapType, List<IJavaExpression> keys, List<IJavaExpression> maps)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constructNew(CodePosition position, IMethodScope<IJavaExpression> scope, TypeInstance<IJavaExpression> type, IMethod<IJavaExpression> method, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression functionExpression(CodePosition position, IMethodScope<IJavaExpression> scope, MethodHeader<IJavaExpression> header, List<Statement<IJavaExpression>> statements)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression staticMethodValue(CodePosition position, IMethodScope<IJavaExpression> scope, IMethod<IJavaExpression> method)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyNot(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyNeg(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyInvert(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyAdd(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anySub(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyCat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyMul(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyDiv(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyMod(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyCastTo(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value, TypeInstance<IJavaExpression> type)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
