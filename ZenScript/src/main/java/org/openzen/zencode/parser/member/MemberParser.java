/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.modifier.ModifierParser;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class MemberParser
{
	private MemberParser() {}
	
	public static List<IParsedMember> parseAllWithClosing(ZenLexer lexer)
	{
		List<IParsedMember> members = new ArrayList<IParsedMember>();
		while (lexer.optional(T_ACLOSE) != null) {
			members.add(parse(lexer));
		}
		return members;
	}
	
	public static IParsedMember parse(ZenLexer lexer)
	{
		List<ParsedAnnotation> annotations = ParsedAnnotation.parseAll(lexer);
		List<IParsedModifier> modifiers = ModifierParser.parseMemberModifiers(lexer);
		
		while (true) {
			switch (lexer.peek().getType()) {
				case T_VAL:
				case T_VAR:
					return compileVarOrVal(lexer, annotations, modifiers);
				case T_FUNCTION:
					return compileFunction(lexer, annotations, modifiers);
				case T_IMPLEMENTS:
					return compileImplements(lexer, annotations, modifiers);
				case T_AS:
					return compileCaster(lexer, annotations, modifiers);
				case T_THIS:
					return compileConstructor(lexer, annotations, modifiers);
				default:
					lexer.error(lexer.getPosition(), "Invalid token for member: " + lexer.next());
					return null;
			}
		}
	}
	
	private static IParsedMember compileVarOrVal(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		Token token = lexer.next();
		String name = lexer.requiredIdentifier();
		IParsedType asType = null;
		if (lexer.optional(T_AS) != null)
			asType = TypeParser.parse(lexer);
		
		ParsedExpression initializer = null;
		if (lexer.optional(T_ASSIGN) != null)
			initializer = ParsedExpression.parse(lexer);
		
		List<ParsedAccessor> accessors = new ArrayList<ParsedAccessor>();
		if (lexer.optional(T_AOPEN) != null) {
			while (lexer.optional(T_ACLOSE) != null) {
				CodePosition position = lexer.getPosition();
				List<ParsedAnnotation> accessorAnnotations = ParsedAnnotation.parseAll(lexer);
				List<IParsedModifier> accessorModifiers = ModifierParser.parseMemberModifiers(lexer);
				ParsedAccessor.Type type;
				if (lexer.optional(T_GET) != null) {
					type = ParsedAccessor.Type.GET;
				} else {
					lexer.required(T_SET, "get or set expected");
					type = ParsedAccessor.Type.SET;
				}
				ParsedStatement contents = ParsedStatement.parse(lexer);
				accessors.add(new ParsedAccessor(position, type, accessorAnnotations, accessorModifiers, contents));
			}
		} else {
			lexer.required(T_SEMICOLON, "; expected");
		}

		if (token.getType() == T_VAL)
			return new ParsedVal(token.getPosition(), annotations, modifiers, name, asType, initializer, accessors);
		else if (token.getType() == T_VAR)
			return new ParsedVar(token.getPosition(), annotations, modifiers, name, asType, initializer, accessors);

		throw new AssertionError();
	}
	
	private static IParsedMember compileFunction(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		String name = lexer.requiredIdentifier();
		ParsedFunctionSignature signature = ParsedFunctionSignature.parse(lexer);
		ParsedStatement contents = ParsedStatement.parse(lexer);
		
		return new ParsedFunctionMember(position, annotations, modifiers, name, signature, contents);
	}
	
	private static IParsedMember compileImplements(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		IParsedType type = TypeParser.parse(lexer);
		lexer.required(T_AOPEN, "{ expected");

		List<IParsedMember> members = new ArrayList<IParsedMember>();
		while (lexer.optional(T_ACLOSE) == null) {
			members.add(parse(lexer));
		}

		return new ParsedImplementation(position, annotations, modifiers, type, members);
	}
	
	private static IParsedMember compileCaster(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		IParsedType asType = TypeParser.parse(lexer);
		ParsedStatement content = ParsedStatement.parse(lexer);
		return new ParsedCaster(position, annotations, modifiers, asType, content);
	}
	
	private static IParsedMember compileConstructor(
			ZenLexer lexer, 
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		ParsedFunctionSignature signature = ParsedFunctionSignature.parse(lexer);
		ParsedStatement content = ParsedStatement.parse(lexer);
		return new ParsedConstructor(position, annotations, modifiers, signature, content);
	}
}
