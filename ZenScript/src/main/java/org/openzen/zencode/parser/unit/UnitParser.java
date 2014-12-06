/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.lexer.ParseException;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.parser.elements.ParsedGenericParameter;
import org.openzen.zencode.parser.elements.ParsedGenericParameters;
import org.openzen.zencode.parser.expression.ParsedCallArguments;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.member.MemberParser;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class UnitParser
{
	private UnitParser() {}
	
	public static IParsedUnit parse(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		switch (lexer.peek().getType()) {
			case T_STRUCT:
				return parseStruct(lexer, annotations, modifiers);
			case T_CLASS:
			case T_INTERFACE:
				return parseClassOrInterface(lexer, annotations, modifiers);
			case T_ENUM:
				return parseEnum(lexer, annotations, modifiers);
			case T_FUNCTION:
				return parseFunction(lexer, annotations, modifiers);
			case T_EXPAND:
				return parseExpansion(lexer, annotations, modifiers);
			default:
				throw new ParseException(lexer.peek(), "Illegal token for compile unit: " + lexer.peek().getValue());
		}
	}
	
	private static IParsedUnit parseStruct(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		String name = lexer.requiredIdentifier();
		
		lexer.required(T_AOPEN, "{ expected");
		List<IParsedMember> members = MemberParser.parseAllWithClosing(lexer);
		
		return new ParsedStruct(position, annotations, modifiers, name, members);
	}
	
	private static IParsedUnit parseClassOrInterface(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		Token start = lexer.next();
		String name = lexer.requiredIdentifier();
		List<ParsedGenericParameter> genericParameters = ParsedGenericParameters.parse(lexer);
		List<IParsedType> extendsTypes = parseExtends(lexer);
		
		lexer.required(T_AOPEN, "{ expected");
		List<IParsedMember> members = MemberParser.parseAllWithClosing(lexer);
		
		if (start.getType() == T_CLASS) {
			return new ParsedClass(
					start.getPosition(),
					annotations,
					modifiers,
					name,
					genericParameters,
					extendsTypes,
					members);
		} else {
			return new ParsedInterface(
					start.getPosition(),
					annotations,
					modifiers,
					name,
					genericParameters,
					extendsTypes,
					members);
		}
	}
	
	private static List<IParsedType> parseExtends(ZenLexer lexer)
	{
		if (lexer.optional(T_EXTENDS) == null)
			return Collections.emptyList();
		
		List<IParsedType> extendsTypes = new ArrayList<IParsedType>();
		extendsTypes.add(TypeParser.parse(lexer));
		
		while (lexer.optional(T_COMMA) != null) {
			extendsTypes.add(TypeParser.parse(lexer));
		}
		
		return extendsTypes;
	}
	
	private static IParsedUnit parseEnum(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		String name = lexer.requiredIdentifier();
		lexer.required(T_AOPEN, "{ expected");

		List<ParsedEnum.Value> values = parseEnumValues(lexer);

		List<IParsedMember> members;
		if (lexer.optional(T_SEMICOLON) != null) {
			members = MemberParser.parseAllWithClosing(lexer);
		} else {
			members = Collections.emptyList();
			lexer.required(T_ACLOSE, "} expected");
		}

		return new ParsedEnum(position, annotations, modifiers, name, values, members);
	}
	
	private static List<ParsedEnum.Value> parseEnumValues(ZenLexer lexer)
	{
		List<ParsedEnum.Value> values = new ArrayList<ParsedEnum.Value>();

		do {
			if (lexer.peek().getType() == T_SEMICOLON || lexer.peek().getType() == T_ACLOSE)
				break;

			String valueName = lexer.requiredIdentifier();
			ParsedCallArguments arguments = null;
			if (lexer.peek().getType() == T_BROPEN)
				arguments = ParsedCallArguments.parse(lexer);

			values.add(new ParsedEnum.Value(valueName, arguments));
		} while (lexer.optional(T_COMMA) != null);
		
		return values;
	}
	
	private static IParsedUnit parseFunction(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		String name = lexer.requiredIdentifier();
		ParsedFunctionSignature signature = ParsedFunctionSignature.parse(lexer);
		ParsedStatement statement = ParsedStatement.parse(lexer);
		
		return new ParsedFunction(position, annotations, modifiers, name, signature, statement);
	}
	
	private static IParsedUnit parseExpansion(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		IParsedType type = TypeParser.parse(lexer);

		List<IParsedMember> members;
		if (lexer.optional(T_AOPEN) != null)
			members = MemberParser.parseAllWithClosing(lexer);
		else
			members = Collections.singletonList(MemberParser.parse(lexer));
		
		return new ParsedExpansion(position, annotations, modifiers, type, members);
	}
}
