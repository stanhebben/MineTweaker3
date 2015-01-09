/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.member;

import java.util.List;
import org.openzen.zencode.parser.unit.IParsedDefinition;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 */
public class ParsedInner implements IParsedMember
{
	private final IParsedDefinition unit;
	
	public ParsedInner(IParsedDefinition unit)
	{
		this.unit = unit;
	}

	@Override
	public <E extends IPartialExpression<E>> void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope)
	{
		ISymbolicDefinition<E> compiled = unit.compile(scope);
		units.add(compiled);
		
		compiled.collectInnerDefinitions(units, scope);
	}

	@Override
	public <E extends IPartialExpression<E>> IMember<E> compile(IDefinitionScope<E> scope)
	{
		return null;
	}
}
