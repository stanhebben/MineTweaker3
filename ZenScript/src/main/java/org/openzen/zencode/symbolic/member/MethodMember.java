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
import stanhebben.zenscript.statements.Statement;

/**
 *
 * @author Stan
 */
public class MethodMember implements IMember
{
	private final ISymbolicUnit unit;
	private final int modifiers;
	private final String name;
	private final MethodHeader header;
	private final List<Statement> statements;
	
	public MethodMember(ISymbolicUnit unit, int modifiers, String name, MethodHeader header)
	{
		this.unit = unit;
		this.modifiers = modifiers;
		this.name = name;
		this.header = header;
		statements = new ArrayList<Statement>();
	}
	
	public void addStatement(Statement statement)
	{
		statements.add(statement);
	}
	
	public MethodHeader getHeader()
	{
		return header;
	}

	@Override
	public ISymbolicUnit getUnit()
	{
		return unit;
	}
}
