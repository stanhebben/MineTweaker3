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
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 * Constructs a java expression compiler.
 * 
 * @author Stan
 */
public class JavaExpressionCompiler implements IExpressionCompiler<IJavaExpression, IJavaType>
{
	public JavaExpressionCompiler()
	{
		
	}

	@Override
	public IJavaExpression invalid(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invalid(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IZenType<IJavaExpression, IJavaType> valueType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantNull(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantBool(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, boolean value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantByte(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, byte value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantUByte(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantShort(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, short value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantUShort(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantInt(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantUInt(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantLong(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, long value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantULong(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, long value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantFloat(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, float value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantDouble(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, double value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantChar(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, int value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constantString(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, String value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constant(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, Object value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression[] constants(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, Object[] values)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression localGet(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, SymbolLocal<IJavaExpression, IJavaType> local)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression localSet(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, SymbolLocal<IJavaExpression, IJavaType> local, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andAnd(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orOr(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareGeneric(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression value, CompareType comparator)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ternary(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression condition, IJavaExpression ifValue, IJavaExpression elseValue)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression notNull(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression range(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression from, IJavaExpression to)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression array(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaType arrayType, List<IJavaExpression> values)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression map(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaType mapType, List<IJavaExpression> keys, List<IJavaExpression> maps)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constructNew(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IZenType<IJavaExpression, IJavaType> type, IMethod<IJavaExpression, IJavaType> method, IJavaExpression[] arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression functionExpression(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, MethodHeader<IJavaExpression, IJavaType> header, List<Statement<IJavaExpression, IJavaType>> statements)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression staticMethodValue(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IMethod<IJavaExpression, IJavaType> method)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyNot(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyNeg(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyInvert(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyAdd(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anySub(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyCat(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyMul(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyDiv(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyMod(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyCastTo(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression value, IJavaType type)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
