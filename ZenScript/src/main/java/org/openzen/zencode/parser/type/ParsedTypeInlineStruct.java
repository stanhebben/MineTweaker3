/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.type;

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
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.SymbolicStruct;

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
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> T compile(IModuleScope<E, T> environment)
	{
		SymbolicStruct<E, T> struct = new SymbolicStruct<E, T>(Modifier.EXPORT.getCode(), environment);
		IDefinitionScope<E, T> scope = new DefinitionScope<E, T>(environment, struct);
		for (IParsedMember member : members) {
			IMember<E, T> compiledMember = member.compile(scope);
			if (compiledMember != null)
				struct.addMember(compiledMember);
		}
		
		return environment.getTypeCompiler().getStruct(scope, struct);
	}
}
