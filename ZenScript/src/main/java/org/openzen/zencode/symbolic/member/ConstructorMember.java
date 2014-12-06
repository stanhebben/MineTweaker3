/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.unit.ISymbolicUnit;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ConstructorMember<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements IMember<E, T>
{
	private final ISymbolicUnit<E, T> unit;
	private final MethodHeader<E, T> header;
	private final List<Statement<E, T>> statements;
	
	public ConstructorMember(ISymbolicUnit<E, T> unit, MethodHeader<E, T> header)
	{
		this.unit = unit;
		this.header = header;
		this.statements = new ArrayList<Statement<E, T>>();
	}
	
	public void addStatement(Statement<E, T> statement)
	{
		statements.add(statement);
	}
	
	// #################################
	// ### Implementation of IMember ###
	// #################################

	@Override
	public ISymbolicUnit<E, T> getUnit()
	{
		return unit;
	}
}
