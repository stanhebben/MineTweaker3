/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.lexer.ParseException;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.definition.ParsedFunctionSignature;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.Strings;
import static org.openzen.zencode.util.Strings.unescape;

/**
 *
 * @author Stanneke
 */
public abstract class ParsedExpression
{
	public static ParsedExpression parse(String value, ICodeErrorLogger<?> errorLogger)
	{
		try {
			return parse(new ZenLexer(errorLogger, value));
		} catch (IOException ex) {
			throw new RuntimeException("Could not parse statement " + value, ex);
		}
	}
	
	public static IAny evalToAny(String value, IModuleScope<?> scope)
	{
		ParsedExpression parsed = parse(value, scope.getErrorLogger());
		return parsed
				.compile(scope.getConstantScope(), null)
				.getCompileTimeValue();
	}
	
	public static ParsedExpression parse(ZenLexer lexer)
	{
		return readAssignExpression(lexer);
	}

	private static ParsedExpression readAssignExpression(ZenLexer lexer)
	{
		Token token = lexer.peek();
		if (token == null) {
			lexer.getErrorLogger().errorUnexpectedEndOfFile(lexer.getPosition());
			return new ParsedExpressionInvalid(lexer.getPosition());
		}

		CodePosition position = token.getPosition();

		ParsedExpression left = readConditionalExpression(position, lexer);

		if (lexer.peek() == null) {
			lexer.getErrorLogger().errorUnexpectedEndOfFile(lexer.getPosition());
			return new ParsedExpressionInvalid(lexer.getPosition());
		}

		switch (lexer.peek().getType()) {
			case T_ASSIGN:
				lexer.next();
				return new ParsedExpressionAssign(position, left, readAssignExpression(lexer));
			case T_PLUSASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.ADD);
			case T_MINUSASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.SUB);
			case T_TILDEASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.CAT);
			case T_MULASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.MUL);
			case T_DIVASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.DIV);
			case T_MODASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.MOD);
			case T_ORASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.OR);
			case T_ANDASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.AND);
			case T_XORASSIGN:
				lexer.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(lexer), OperatorType.XOR);
		}

		return left;
	}

	private static ParsedExpression readConditionalExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression left = readOrOrExpression(position, lexer);

		if (lexer.optional(T_QUEST) != null) {
			ParsedExpression onIf = readOrOrExpression(lexer.peek().getPosition(), lexer);
			lexer.required(T_COLON, ": expected");
			ParsedExpression onElse = readConditionalExpression(lexer.peek().getPosition(), lexer);
			return new ParsedExpressionConditional(position, left, onIf, onElse);
		}

		return left;
	}

	private static ParsedExpression readOrOrExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression left = readAndAndExpression(position, lexer);

		while (lexer.optional(T_OR2) != null) {
			ParsedExpression right = readAndAndExpression(lexer.peek().getPosition(), lexer);
			left = new ParsedExpressionOrOr(position, left, right);
		}
		return left;
	}

	private static ParsedExpression readAndAndExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression left = readOrExpression(position, lexer);

		while (lexer.optional(T_AND2) != null) {
			ParsedExpression right = readOrExpression(lexer.peek().getPosition(), lexer);
			left = new ParsedExpressionAndAnd(position, left, right);
		}
		return left;
	}

	private static ParsedExpression readOrExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression left = readXorExpression(position, lexer);

		while (lexer.optional(T_OR) != null) {
			ParsedExpression right = readXorExpression(lexer.peek().getPosition(), lexer);
			left = new ParsedExpressionBinary(position, left, right, OperatorType.OR);
		}
		return left;
	}

	private static ParsedExpression readXorExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression left = readAndExpression(position, lexer);

		while (lexer.optional(T_XOR) != null) {
			ParsedExpression right = readAndExpression(lexer.peek().getPosition(), lexer);
			left = new ParsedExpressionBinary(position, left, right, OperatorType.XOR);
		}
		return left;
	}

	private static ParsedExpression readAndExpression(CodePosition position, ZenLexer parser)
	{
		ParsedExpression left = readCompareExpression(position, parser);

		while (parser.optional(T_AND) != null) {
			ParsedExpression right = readCompareExpression(parser.peek().getPosition(), parser);
			left = new ParsedExpressionBinary(position, left, right, OperatorType.AND);
		}
		return left;
	}

	private static ParsedExpression readCompareExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression left = readAddExpression(position, lexer);

		switch (lexer.peek() == null ? -1 : lexer.peek().getType()) {
			case T_EQ: {
				lexer.next();
				ParsedExpression right = readAddExpression(lexer.peek().getPosition(), lexer);
				return new ParsedExpressionCompare(position, left, right, CompareType.EQ);
			}
			case T_NOTEQ: {
				lexer.next();
				ParsedExpression right = readAddExpression(lexer.peek().getPosition(), lexer);
				return new ParsedExpressionCompare(position, left, right, CompareType.NE);
			}
			case T_LT: {
				lexer.next();
				ParsedExpression right = readAddExpression(lexer.peek().getPosition(), lexer);
				return new ParsedExpressionCompare(position, left, right, CompareType.LT);
			}
			case T_LTEQ: {
				lexer.next();
				ParsedExpression right = readAddExpression(lexer.peek().getPosition(), lexer);
				return new ParsedExpressionCompare(position, left, right, CompareType.LE);
			}
			case T_GT: {
				lexer.next();
				ParsedExpression right = readAddExpression(lexer.peek().getPosition(), lexer);
				return new ParsedExpressionCompare(position, left, right, CompareType.GT);
			}
			case T_GTEQ: {
				lexer.next();
				ParsedExpression right = readAddExpression(lexer.peek().getPosition(), lexer);
				return new ParsedExpressionCompare(position, left, right, CompareType.GE);
			}
			case T_IN: {
				lexer.next();
				ParsedExpression right = readAddExpression(lexer.peek().getPosition(), lexer);
				return new ParsedExpressionBinary(position, left, right, OperatorType.CONTAINS);
			}
		}

		return left;
	}

	private static ParsedExpression readAddExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression left = readMulExpression(position, lexer);

		while (true) {
			if (lexer.optional(T_PLUS) != null) {
				ParsedExpression right = readMulExpression(lexer.peek().getPosition(), lexer);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.ADD);
			} else if (lexer.optional(T_MINUS) != null) {
				ParsedExpression right = readMulExpression(lexer.peek().getPosition(), lexer);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.SUB);
			} else if (lexer.optional(T_TILDE) != null) {
				ParsedExpression right = readMulExpression(lexer.peek().getPosition(), lexer);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.CAT);
			} else
				break;
		}
		return left;
	}

	private static ParsedExpression readMulExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression left = readUnaryExpression(position, lexer);

		while (true) {
			if (lexer.optional(T_MUL) != null) {
				ParsedExpression right = readUnaryExpression(lexer.peek().getPosition(), lexer);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.MUL);
			} else if (lexer.optional(T_DIV) != null) {
				ParsedExpression right = readUnaryExpression(lexer.peek().getPosition(), lexer);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.DIV);
			} else if (lexer.optional(T_MOD) != null) {
				ParsedExpression right = readUnaryExpression(lexer.peek().getPosition(), lexer);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.MOD);
			} else
				break;
		}

		return left;
	}

	private static ParsedExpression readUnaryExpression(CodePosition position, ZenLexer lexer)
	{
		switch (lexer.peek().getType()) {
			case T_NOT:
				lexer.next();
				return new ParsedExpressionUnary(
						position,
						readUnaryExpression(lexer.peek().getPosition(), lexer),
						OperatorType.NOT);

			case T_MINUS:
				lexer.next();
				return new ParsedExpressionUnary(
						position,
						readUnaryExpression(lexer.peek().getPosition(), lexer),
						OperatorType.NEG);

			case T_TILDE:
				lexer.next();
				return new ParsedExpressionUnary(
						position,
						readUnaryExpression(lexer.peek().getPosition(), lexer),
						OperatorType.INVERT);

			default:
				return readPostfixExpression(position, lexer);
		}
	}

	private static ParsedExpression readPostfixExpression(CodePosition position, ZenLexer lexer)
	{
		ParsedExpression base = readPrimaryExpression(position, lexer);

		outer:
		while (lexer.hasNext()) {
			switch (lexer.peek().getType()) {
				case T_DOT:
					lexer.next();
					Token indexString = lexer.optional(TOKEN_ID);
					if (indexString != null)
						base = new ParsedExpressionMember(position, base, indexString.getValue());
					else {
						Token indexString2 = lexer.optional(T_STRING);
						if (indexString2 != null)
							base = new ParsedExpressionMember(position, base, Strings.unescape(indexString2.getValue()));
						else {
							Token last = lexer.next();
							throw new ParseException(last, "Invalid expression, last token: " + last.getValue());
						}
					}
					break;

				case T_DOT2:
					lexer.next();
					ParsedExpression to = readAssignExpression(lexer);
					base = new ParsedExpressionRange(base, to);
					break;

				case T_SQBROPEN:
					lexer.next();
					ParsedExpression index = readAssignExpression(lexer);
					base = new ParsedExpressionIndex(position, base, index);
					lexer.required(T_SQBRCLOSE, "] expected");
					break;

				case T_BROPEN:
					ParsedCallArguments callArguments = ParsedCallArguments.parse(lexer);
					base = new ParsedExpressionCall(base.getPosition(), base, callArguments);
					break;

				case T_AS:
					lexer.next();
					IParsedType type = TypeParser.parse(lexer);
					base = new ParsedExpressionCast(position, base, type);
					break;

				default:
					break outer;
			}
		}

		return base;
	}

	private static ParsedExpression readPrimaryExpression(CodePosition position, ZenLexer lexer)
	{
		switch (lexer.peek().getType()) {
			case TOKEN_INTVALUE:
				return new ParsedExpressionInt(
						position,
						Long.parseLong(lexer.next().getValue()));

			case T_FLOATVALUE:
				return new ParsedExpressionFloat(
						position,
						Double.parseDouble(lexer.next().getValue()));

			case T_STRINGVALUE:
				return new ParsedExpressionString(
						position,
						unescape(lexer.next().getValue()));

			case T_DOLLAR: {
				if (lexer.isNext(T_STRINGVALUE))
					return new ParsedExpressionDollar(
							position,
							unescape(lexer.next().getValue()));
				else if (lexer.isNext(TOKEN_ID))
					return new ParsedExpressionDollar(
							position,
							lexer.next().getValue());
				else
					return new ParsedExpressionDollar(
							position,
							null);
			}

			case TOKEN_ID:
				return new ParsedExpressionVariable(
						position,
						lexer.next().getValue());

			case T_FUNCTION:
				// function (argname, argname, ...) { ...contents... }
				lexer.next();

				ParsedFunctionSignature header = ParsedFunctionSignature.parse(lexer);

				lexer.required(T_AOPEN, "{ expected");

				List<ParsedStatement> statements = new ArrayList<ParsedStatement>();
				if (lexer.optional(T_ACLOSE) == null)
					while (lexer.optional(T_ACLOSE) == null) {
						statements.add(ParsedStatement.parse(lexer));
					}

				return new ParsedExpressionFunction(position, header, statements);

			case T_LT: {
				Token start = lexer.next();
				List<Token> tokens = new ArrayList<Token>();
				Token next = lexer.next();
				while (next.getType() != ZenLexer.T_GT) {
					tokens.add(next);
					next = lexer.next();
				}
				return new ParsedExpressionBracket(start.getPosition(), tokens);
			}
			case T_SQBROPEN: {
				lexer.next();
				List<ParsedExpression> contents = new ArrayList<ParsedExpression>();
				if (lexer.optional(T_SQBRCLOSE) == null)
					while (lexer.optional(T_SQBRCLOSE) == null) {
						contents.add(readAssignExpression(lexer));
						if (lexer.optional(T_COMMA) == null) {
							lexer.required(T_SQBRCLOSE, "] or , expected");
							break;
						}
					}
				return new ParsedExpressionArray(position, contents);
			}
			case T_AOPEN: {
				lexer.next();

				List<ParsedExpression> keys = new ArrayList<ParsedExpression>();
				List<ParsedExpression> values = new ArrayList<ParsedExpression>();
				if (lexer.optional(T_ACLOSE) == null) {
					while (lexer.optional(T_ACLOSE) == null) {
						ParsedExpression key = readAssignExpression(lexer);
						
						if (lexer.optional(T_COLON) != null) {
							keys.add(key);
							values.add(readAssignExpression(lexer));
						} else {
							keys.add(null);
							values.add(key);
						}

						if (lexer.optional(T_COMMA) == null) {
							lexer.required(T_ACLOSE, "} or , expected");
							break;
						}
					}

					return new ParsedExpressionAssociative(position, keys, values);
				}
			}
			case T_NEW: {
				Token newToken = lexer.next();
				IParsedType type = TypeParser.parse(lexer);
				ParsedCallArguments arguments = ParsedCallArguments.parse(lexer);
				return new ParsedExpressionNew(newToken.getPosition(), type, arguments);
			}
			case T_TRUE:
				lexer.next();
				return new ParsedExpressionBool(position, true);
			case T_FALSE:
				lexer.next();
				return new ParsedExpressionBool(position, false);
			case T_NULL:
				lexer.next();
				return new ParsedExpressionNull(position);
			case T_BROPEN:
				lexer.next();
				ParsedExpression result = readAssignExpression(lexer);
				lexer.required(T_BRCLOSE, ") expected");
				return result;
			default:
				Token last = lexer.next();
				throw new ParseException(last, "Invalid expression, last token: " + last.getValue());
		}
	}

	private final CodePosition position;

	public ParsedExpression(CodePosition position)
	{
		this.position = position;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public abstract <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> environment, IGenericType<E> predictedType);

	public final <E extends IPartialExpression<E>>
		 E compile(IMethodScope<E> environment, IGenericType<E> predictedType)
	{
		return compilePartial(environment, predictedType).eval();
	}

	public String asIdentifier()
	{
		return null;
	}

	public <E extends IPartialExpression<E>>
		 E compileKey(IMethodScope<E> environment, IGenericType<E> asType)
	{
		return compile(environment, asType);
	}

	public abstract IAny eval(IZenCompileEnvironment<?> environment);
}
