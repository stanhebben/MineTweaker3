/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicStruct<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements ISymbolicUnit<E, T>
{
	private final List<IMember<E, T>> members;
	
	public SymbolicStruct()
	{
		this.members = new ArrayList<IMember<E, T>>();
	}
	
	public void addMember(IMember<E, T> member)
	{
		members.add(member);
	}
	
	public List<IMember<E, T>> getMembers()
	{
		return members;
	}
}
