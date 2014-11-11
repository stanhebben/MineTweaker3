/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.unit.ISymbolicUnit;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.Modifiers;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionGetInstanceField;
import stanhebben.zenscript.expression.ExpressionGetStaticField;
import stanhebben.zenscript.expression.ExpressionSetInstanceField;
import stanhebben.zenscript.expression.ExpressionSetStaticField;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class FieldMember implements IMember, IField
{
	private final ISymbolicUnit unit;
	
	private final int access;
	private final String name;
	private final ZenType type;
	
	public FieldMember(ISymbolicUnit unit, int access, String name, ZenType type)
	{
		this.unit = unit;
		this.access = access;
		this.name = name;
		this.type = type;
	}

	@Override
	public ISymbolicUnit getUnit()
	{
		return unit;
	}
	
	@Override
	public Expression makeStaticGetExpression(CodePosition position, IScopeMethod scope)
	{
		return new ExpressionGetStaticField(position, scope, this);
	}
	
	@Override
	public Expression makeStaticSetExpression(CodePosition position, IScopeMethod scope, Expression value)
	{
		return new ExpressionSetStaticField(position, scope, this, value);
	}
	
	@Override
	public Expression makeInstanceGetExpression(CodePosition position, IScopeMethod scope, Expression target)
	{
		return new ExpressionGetInstanceField(position, scope, target, this);
	}
	
	@Override
	public Expression makeInstanceSetExpression(CodePosition position, IScopeMethod scope, Expression target, Expression value)
	{
		return new ExpressionSetInstanceField(position, scope, target, this, value);
	}
	
	// ############################
	// ### Field implementation ###
	// ############################
	
	@Override
	public ZenType getType()
	{
		return type;
	}

	@Override
	public boolean isFinal()
	{
		return (access & Modifiers.FINAL) > 0;
	}

	@Override
	public boolean isStatic()
	{
		return (access & Modifiers.STATIC) > 0;
	}

	@Override
	public void compileStaticGet(MethodOutput output)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void compileStaticSet(MethodOutput output)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void compileInstanceGet(MethodOutput output)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void compileInstanceSet(MethodOutput output)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
