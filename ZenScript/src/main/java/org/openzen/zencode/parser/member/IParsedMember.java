/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 */
public interface IParsedMember
{
	public <E extends IPartialExpression<E>>
		void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope);
	
	public <E extends IPartialExpression<E>>
		 IMember<E> compile(IDefinitionScope<E> scope);
}
