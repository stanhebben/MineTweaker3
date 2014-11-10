/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.member.IMember;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public class SymbolicStruct implements ISymbolicUnit
{
	private final List<IMember> members;
	
	public SymbolicStruct()
	{
		this.members = new ArrayList<IMember>();
	}
	
	public void addMember(IMember member)
	{
		members.add(member);
	}
	
	public List<IMember> getMembers()
	{
		return members;
	}

	@Override
	public ZenType getType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void compile()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
