/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.unit.ISymbolicUnit;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionNew;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeFunction;
import org.openzen.zencode.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class ConstructorMember implements IMember, IMethod
{
	private final ISymbolicUnit unit;
	private final MethodHeader header;
	private final List<Statement> statements;
	private ZenTypeFunction functionType;
	
	public ConstructorMember(ISymbolicUnit unit, MethodHeader header)
	{
		this.unit = unit;
		this.header = header;
		this.statements = new ArrayList<Statement>();
	}
	
	public void addStatement(Statement statement)
	{
		statements.add(statement);
	}
	
	// #################################
	// ### Implementation of IMember ###
	// #################################

	@Override
	public ISymbolicUnit getUnit()
	{
		return unit;
	}
	
	// #################################
	// ### Implementation of IMethod ###
	// #################################
	
	@Override
	public Expression call(CodePosition position, IScopeMethod scope, Expression... arguments)
	{
		return new ExpressionNew(position, scope, unit.getType(), this, arguments);
	}

	@Override
	public boolean isStatic()
	{
		return true;
	}

	@Override
	public void invokeVirtual(MethodOutput output)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void invokeStatic(MethodOutput output)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void invokeSpecial(MethodOutput output)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void invokeVirtual(MethodOutput output, Expression receiver, Expression[] arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void invokeStatic(MethodOutput output, Expression[] arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ZenTypeFunction getFunctionType()
	{
		if (functionType == null)
			functionType = new ZenTypeFunction(header);
		
		return functionType;
	}

	@Override
	public MethodHeader getMethodHeader()
	{
		return header;
	}

	@Override
	public ZenType getReturnType()
	{
		return header.getReturnType();
	}
}
