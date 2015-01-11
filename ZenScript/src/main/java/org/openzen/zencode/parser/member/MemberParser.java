/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.lexer.ParseException;
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
import org.openzen.zencode.parser.definition.IParsedDefinition;
import org.openzen.zencode.parser.definition.DefinitionParser;
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
				case T_STRUCT:
				case T_CLASS:
				case T_INTERFACE:
				case T_ENUM:
					return compileInner(lexer, annotations, modifiers);
				default:
					lexer.getErrorLogger().errorNotAValidMemberToken(lexer.getPosition(), lexer.next());
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
			return new ParsedField(token.getPosition(), annotations, modifiers, name, asType, initializer, accessors, true);
		else if (token.getType() == T_VAR)
			return new ParsedField(token.getPosition(), annotations, modifiers, name, asType, initializer, accessors, false);
		
		throw new AssertionError();
	}
	
	private static IParsedMember compileFunction(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		CodePosition position = lexer.next().getPosition();
		
		switch (lexer.peek().getType()) {
			case T_BROPEN: {
				ParsedFunctionSignature signature = ParsedFunctionSignature.parse(lexer);
				ParsedStatement contents = ParsedStatement.parse(lexer);
				return new ParsedCaller(position, annotations, modifiers, signature, contents);
			}
			case TOKEN_ID: {
				String name = lexer.requiredIdentifier();
				ParsedFunctionSignature signature = ParsedFunctionSignature.parse(lexer);
				ParsedStatement contents = ParsedStatement.parse(lexer);

				return new ParsedFunctionMember(position, annotations, modifiers, name, signature, contents);
			}
			case T_PLUS:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.ADD);
			case T_PLUSASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.ADDASSIGN);
			case T_MINUS:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.SUB);
			case T_MINUSASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.SUBASSIGN);
			case T_MUL:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.MUL);
			case T_MULASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.MULASSIGN);
			case T_DIV:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.DIV);
			case T_DIVASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.DIVASSIGN);
			case T_MOD:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.MOD);
			case T_MODASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.MODASSIGN);
			case T_TILDE:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.CAT);
			case T_TILDEASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.CATASSIGN);
			case T_DOT: {
				lexer.next();
				
				ParsedFunctionSignature signature = ParsedFunctionSignature.parse(lexer);
				if (signature.getParameters().isEmpty())
				{
					signature = ParsedFunctionSignature.parse(lexer);
					ParsedStatement content = ParsedStatement.parse(lexer);
					return new ParsedAnyCaller(position, annotations, modifiers, signature, content);
				}
				else
				{
					ParsedStatement content = ParsedStatement.parse(lexer);
					return new ParsedOperator(position, annotations, modifiers, signature, content, OperatorType.MEMBERGETTER);
				}
			}
			case T_DOTASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.MEMBERSETTER);
			case T_SHL:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.SHL);
			case T_SHLASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.SHLASSIGN);
			case T_SHR:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.SHR);
			case T_SHRASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.SHRASSIGN);
			case T_XOR:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.XOR);
			case T_XORASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.XORASSIGN);
			case T_AND:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.AND);
			case T_ANDASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.ANDASSIGN);
			case T_OR:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.OR);
			case T_ORASSIGN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.ORASSIGN);
			case T_IN:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.CONTAINS);
			case T_DOT2:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.RANGE);
			case T_FOR:
				lexer.next();
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.FOR);
			case T_SQBROPEN:
				lexer.next();
				lexer.required(T_SQBRCLOSE, "] expected");
				return compileOperation(lexer, position, annotations, modifiers, OperatorType.MEMBERSETTER);
			default:
				throw new ParseException(lexer.next(), "identifier, function signature or operator expected");
		}
	}
	
	private static IParsedMember compileOperation(
			ZenLexer lexer,
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			OperatorType operator)
	{
		ParsedFunctionSignature signature = ParsedFunctionSignature.parse(lexer);
		ParsedStatement contents = ParsedStatement.parse(lexer);
		return new ParsedOperator(position, annotations, modifiers, signature, contents, operator);
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
	
	private static IParsedMember compileInner(
			ZenLexer lexer,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		IParsedDefinition unit = DefinitionParser.parse(lexer, annotations, modifiers);
		return new ParsedInner(unit);
	}
}
