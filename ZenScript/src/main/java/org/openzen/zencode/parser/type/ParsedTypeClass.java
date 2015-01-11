/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.Strings;
import org.openzen.zencode.util.CodePosition;

/**
 * Represents a parsed class, interface, enum or struct name. Can contain a
 * generic type.
 *
 * @author Stan Hebben
 */
public class ParsedTypeClass implements IParsedType
{
	private final CodePosition position;
	private final List<String> name;
	private final List<IParsedType> genericType;

	public ParsedTypeClass(ZenLexer lexer)
	{
		Token nameFirst = lexer.required(TOKEN_ID, "identifier expected");
		position = nameFirst.getPosition();

		name = new ArrayList<String>();
		name.add(nameFirst.getValue());

		while (lexer.optional(T_DOT) != null) {
			Token namePart = lexer.required(TOKEN_ID, "identifier expected");
			name.add(namePart.getValue());
		}

		if (lexer.optional(T_LT) == null)
			genericType = null;
		else {
			genericType = new ArrayList<IParsedType>();
			if (lexer.optional(T_GT) == null) {
				do {
					IParsedType type = TypeParser.parse(lexer);
					genericType.add(type);
					
					if (lexer.optional(T_COMMA) == null) {
						lexer.required(T_GT, "> or , expected");
						break;
					}
				} while (true);
			}
		}
	}

	@Override
	public <E extends IPartialExpression<E>>
		 TypeInstance<E> compile(IModuleScope<E> scope)
	{
		IImportable<E> imported;
		if (scope.contains(name.get(0))) {
			imported = scope.getSymbol(name.get(0)).asImportable();
			if (imported == null) {
				scope.getErrorLogger().errorNotAType(position, scope.getSymbol(name.get(0)), name.get(0));
				return scope.getTypeCompiler().getAny(scope);
			}
			
			for (int i = 1; i < name.size(); i++) {
				imported = imported.getSubDefinition(name.get(i));
				if (imported == null) {
					
					return scope.getTypeCompiler().getAny(scope);
				}
			}
		} else {
			imported = scope.getRootPackage().resolve(position, scope.getErrorLogger(), name, false);
		}
		
		List<TypeInstance<E>> compiledGenericTypes;
		if (genericType == null)
			compiledGenericTypes = null;
		else {
			compiledGenericTypes = new ArrayList<TypeInstance<E>>();
			for (IParsedType type : genericType) {
				compiledGenericTypes.add(type.compile(scope));
			}
		}
		
		TypeInstance<E> type = imported.toType(scope, compiledGenericTypes);
		if (type == null) {
			scope.getErrorLogger().errorNotAType(position, imported);
		}
		return type;
	}
	
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(Strings.join(name, "."));
		
		if (genericType != null) {
			result.append("<");
			
			boolean first = true;
			for (IParsedType parsedType : genericType) {
				if (first) {
					first = false;
				} else {
					result.append(",");
				}
				result.append(parsedType.toString());
			}
			
			result.append(">");
		}
		
		return result.toString();
	}
}
