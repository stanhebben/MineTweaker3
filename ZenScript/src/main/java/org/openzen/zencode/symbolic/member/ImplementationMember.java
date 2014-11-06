/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.unit.ISymbolicUnit;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public class ImplementationMember implements IMember
{
	private final ISymbolicUnit unit;
	private final ZenType interfaceType;
	private final List<IMember> members;
	
	public ImplementationMember(ISymbolicUnit unit, ZenType interfaceType)
	{
		this.unit = unit;
		this.interfaceType = interfaceType;
		members = new ArrayList<IMember>();
	}
	
	public void addMember(IMember member)
	{
		members.add(member);
	}

	@Override
	public ISymbolicUnit getUnit()
	{
		return unit;
	}
}
