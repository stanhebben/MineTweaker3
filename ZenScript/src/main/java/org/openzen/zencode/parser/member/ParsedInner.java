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
import org.openzen.zencode.symbolic.type.IZenType;
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
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> void collectInnerDefinitions(List<ISymbolicDefinition<E, T>> units, IModuleScope<E, T> scope)
	{
		ISymbolicDefinition<E, T> compiled = unit.compile(scope);
		units.add(compiled);
		
		compiled.collectInnerDefinitions(units, scope);
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> IMember<E, T> compile(IDefinitionScope<E, T> scope)
	{
		return null;
	}
}
