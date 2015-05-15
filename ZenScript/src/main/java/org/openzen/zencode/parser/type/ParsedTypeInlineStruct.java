/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.type;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_AOPEN;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.member.MemberParser;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.DefinitionScope;
import org.openzen.zencode.symbolic.definition.SymbolicStruct;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeDefinition;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;

/**
 *
 * @author Stan
 */
public class ParsedTypeInlineStruct implements IParsedType
{
	private final List<IParsedMember> members;
	
	public ParsedTypeInlineStruct(ZenLexer lexer)
	{
		lexer.required(T_AOPEN, "{ expected");
		members = MemberParser.parseAllWithClosing(lexer);
	}
	
	@Override
	public <E extends IPartialExpression<E>> IGenericType<E> compile(IModuleScope<E> environment)
	{
		SymbolicStruct<E> struct = new SymbolicStruct<>(Modifier.EXPORT.getCode(), environment);
		IDefinitionScope<E> scope = new DefinitionScope<>(environment, struct);
		TypeDefinition<E> typeDefinition = new TypeDefinition<>(Collections.emptyList(), true, false);
		for (IParsedMember member : members) {
			IMember<E> compiledMember = member.compile(scope);
			if (compiledMember != null) {
				struct.addMember(compiledMember);
				typeDefinition.addMember(compiledMember);
			}
		}
		
		return new TypeInstance<>(typeDefinition, TypeCapture.empty(), false);
	}
}
