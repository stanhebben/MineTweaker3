/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ParseException;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.runtime.IAny;
import static org.openzen.zencode.util.Strings.unescapeString;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public abstract class ParsedExpression
{
	public static ParsedExpression parse(ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		return readAssignExpression(parser, errorLogger);
	}

	private static ParsedExpression readAssignExpression(ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		Token token = parser.peek();
		if (token == null) {
			CodePosition position = new CodePosition(parser.getFile(), parser.getLine(), parser.getLineOffset());
			errorLogger.error(position, "unexpected end of file; expression expected");
			return new ParsedExpressionInvalid(position);
		}

		CodePosition position = token.getPosition();

		ParsedExpression left = readConditionalExpression(position, parser, errorLogger);

		if (parser.peek() == null) {
			CodePosition position2 = new CodePosition(parser.getFile(), parser.getLine(), parser.getLineOffset());
			errorLogger.error(position2, "unexpected end of file - ; expected");
			return new ParsedExpressionInvalid(position2);
		}

		switch (parser.peek().getType()) {
			case T_ASSIGN:
				parser.next();
				return new ParsedExpressionAssign(position, left, readAssignExpression(parser, errorLogger));
			case T_PLUSASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.ADD);
			case T_MINUSASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.SUB);
			case T_TILDEASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.CAT);
			case T_MULASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.MUL);
			case T_DIVASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.DIV);
			case T_MODASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.MOD);
			case T_ORASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.OR);
			case T_ANDASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.AND);
			case T_XORASSIGN:
				parser.next();
				return new ParsedExpressionOpAssign(position, left, readAssignExpression(parser, errorLogger), OperatorType.XOR);
		}

		return left;
	}

	private static ParsedExpression readConditionalExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readOrOrExpression(position, parser, errorLogger);

		if (parser.optional(T_QUEST) != null) {
			ParsedExpression onIf = readOrOrExpression(parser.peek().getPosition(), parser, errorLogger);
			parser.required(T_COLON, ": expected");
			ParsedExpression onElse = readConditionalExpression(parser.peek().getPosition(), parser, errorLogger);
			return new ParsedExpressionConditional(position, left, onIf, onElse);
		}

		return left;
	}

	private static ParsedExpression readOrOrExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readAndAndExpression(position, parser, errorLogger);

		while (parser.optional(T_OR2) != null) {
			ParsedExpression right = readAndAndExpression(parser.peek().getPosition(), parser, errorLogger);
			left = new ParsedExpressionOrOr(position, left, right);
		}
		return left;
	}

	private static ParsedExpression readAndAndExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readOrExpression(position, parser, errorLogger);

		while (parser.optional(T_AND2) != null) {
			ParsedExpression right = readOrExpression(parser.peek().getPosition(), parser, errorLogger);
			left = new ParsedExpressionAndAnd(position, left, right);
		}
		return left;
	}

	private static ParsedExpression readOrExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readXorExpression(position, parser, errorLogger);

		while (parser.optional(T_OR) != null) {
			ParsedExpression right = readXorExpression(parser.peek().getPosition(), parser, errorLogger);
			left = new ParsedExpressionBinary(position, left, right, OperatorType.OR);
		}
		return left;
	}

	private static ParsedExpression readXorExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readAndExpression(position, parser, errorLogger);

		while (parser.optional(T_XOR) != null) {
			ParsedExpression right = readAndExpression(parser.peek().getPosition(), parser, errorLogger);
			left = new ParsedExpressionBinary(position, left, right, OperatorType.XOR);
		}
		return left;
	}

	private static ParsedExpression readAndExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readCompareExpression(position, parser, errorLogger);

		while (parser.optional(T_AND) != null) {
			ParsedExpression right = readCompareExpression(parser.peek().getPosition(), parser, errorLogger);
			left = new ParsedExpressionBinary(position, left, right, OperatorType.AND);
		}
		return left;
	}

	private static ParsedExpression readCompareExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readAddExpression(position, parser, errorLogger);

		switch (parser.peek() == null ? -1 : parser.peek().getType()) {
			case T_EQ: {
				parser.next();
				ParsedExpression right = readAddExpression(parser.peek().getPosition(), parser, errorLogger);
				return new ParsedExpressionCompare(position, left, right, CompareType.EQ);
			}
			case T_NOTEQ: {
				parser.next();
				ParsedExpression right = readAddExpression(parser.peek().getPosition(), parser, errorLogger);
				return new ParsedExpressionCompare(position, left, right, CompareType.NE);
			}
			case T_LT: {
				parser.next();
				ParsedExpression right = readAddExpression(parser.peek().getPosition(), parser, errorLogger);
				return new ParsedExpressionCompare(position, left, right, CompareType.LT);
			}
			case T_LTEQ: {
				parser.next();
				ParsedExpression right = readAddExpression(parser.peek().getPosition(), parser, errorLogger);
				return new ParsedExpressionCompare(position, left, right, CompareType.LE);
			}
			case T_GT: {
				parser.next();
				ParsedExpression right = readAddExpression(parser.peek().getPosition(), parser, errorLogger);
				return new ParsedExpressionCompare(position, left, right, CompareType.GT);
			}
			case T_GTEQ: {
				parser.next();
				ParsedExpression right = readAddExpression(parser.peek().getPosition(), parser, errorLogger);
				return new ParsedExpressionCompare(position, left, right, CompareType.GE);
			}
			case T_IN: {
				parser.next();
				ParsedExpression right = readAddExpression(parser.peek().getPosition(), parser, errorLogger);
				return new ParsedExpressionBinary(position, left, right, OperatorType.CONTAINS);
			}
		}

		return left;
	}

	private static ParsedExpression readAddExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readMulExpression(position, parser, errorLogger);

		while (true) {
			if (parser.optional(T_PLUS) != null) {
				ParsedExpression right = readMulExpression(parser.peek().getPosition(), parser, errorLogger);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.ADD);
			} else if (parser.optional(T_MINUS) != null) {
				ParsedExpression right = readMulExpression(parser.peek().getPosition(), parser, errorLogger);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.SUB);
			} else if (parser.optional(T_TILDE) != null) {
				ParsedExpression right = readMulExpression(parser.peek().getPosition(), parser, errorLogger);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.CAT);
			} else
				break;
		}
		return left;
	}

	private static ParsedExpression readMulExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression left = readUnaryExpression(position, parser, errorLogger);

		while (true) {
			if (parser.optional(T_MUL) != null) {
				ParsedExpression right = readUnaryExpression(parser.peek().getPosition(), parser, errorLogger);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.MUL);
			} else if (parser.optional(T_DIV) != null) {
				ParsedExpression right = readUnaryExpression(parser.peek().getPosition(), parser, errorLogger);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.DIV);
			} else if (parser.optional(T_MOD) != null) {
				ParsedExpression right = readUnaryExpression(parser.peek().getPosition(), parser, errorLogger);
				left = new ParsedExpressionBinary(position, left, right, OperatorType.MOD);
			} else
				break;
		}

		return left;
	}

	private static ParsedExpression readUnaryExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		switch (parser.peek().getType()) {
			case T_NOT:
				parser.next();
				return new ParsedExpressionUnary(
						position,
						readUnaryExpression(parser.peek().getPosition(), parser, errorLogger),
						OperatorType.NOT);

			case T_MINUS:
				parser.next();
				return new ParsedExpressionUnary(
						position,
						readUnaryExpression(parser.peek().getPosition(), parser, errorLogger),
						OperatorType.NEG);

			case T_TILDE:
				parser.next();
				return new ParsedExpressionUnary(
						position,
						readUnaryExpression(parser.peek().getPosition(), parser, errorLogger),
						OperatorType.INVERT);

			default:
				return readPostfixExpression(position, parser, errorLogger);
		}
	}

	private static ParsedExpression readPostfixExpression(CodePosition position, ZenLexer parser, ICodeErrorLogger errorLogger)
	{
		ParsedExpression base = readPrimaryExpression(position, parser, errorLogger);

		outer:
		while (parser.hasNext()) {
			switch (parser.peek().getType()) {
				case T_DOT:
					parser.next();
					Token indexString = parser.optional(TOKEN_ID);
					if (indexString != null)
						base = new ParsedExpressionMember(position, base, indexString.getValue());
					else {
						Token indexString2 = parser.optional(T_STRING);
						if (indexString2 != null)
							base = new ParsedExpressionMember(position, base, unescapeString(indexString2.getValue()));
						else {
							Token last = parser.next();
							throw new ParseException(last, "Invalid expression, last token: " + last.getValue());
						}
					}
					break;

				case T_DOT2:
					parser.next();
					ParsedExpression to = readAssignExpression(parser, errorLogger);
					base = new ParsedExpressionRange(base, to);
					break;

				case T_SQBROPEN:
					parser.next();
					ParsedExpression index = readAssignExpression(parser, errorLogger);
					base = new ParsedExpressionIndex(position, base, index);
					parser.required(T_SQBRCLOSE, "] expected");
					break;

				case T_BROPEN:
					ParsedCallArguments callArguments = ParsedCallArguments.parse(parser, errorLogger);
					base = new ParsedExpressionCall(base.getPosition(), base, callArguments);
					break;

				case T_AS:
					parser.next();
					IParsedType type = TypeParser.parse(parser, errorLogger);
					base = new ParsedExpressionCast(position, base, type);
					break;

				default:
					break outer;
			}
		}

		return base;
	}

	private static ParsedExpression readPrimaryExpression(CodePosition position, ZenLexer tokener, ICodeErrorLogger errorLogger)
	{
		switch (tokener.peek().getType()) {
			case TOKEN_INTVALUE:
				return new ParsedExpressionInt(
						position,
						Long.parseLong(tokener.next().getValue()));

			case T_FLOATVALUE:
				return new ParsedExpressionFloat(
						position,
						Double.parseDouble(tokener.next().getValue()));

			case T_STRINGVALUE:
				return new ParsedExpressionString(
						position,
						unescapeString(tokener.next().getValue()));

			case T_DOLLAR: {
				if (tokener.isNext(T_STRINGVALUE))
					return new ParsedExpressionDollar(
							position,
							unescapeString(tokener.next().getValue()));
				else if (tokener.isNext(TOKEN_ID))
					return new ParsedExpressionDollar(
							position,
							tokener.next().getValue());
				else
					return new ParsedExpressionDollar(
							position,
							null);
			}

			case TOKEN_ID:
				return new ParsedExpressionVariable(
						position,
						tokener.next().getValue());

			case T_FUNCTION:
				// function (argname, argname, ...) { ...contents... }
				tokener.next();

				ParsedFunctionSignature header = ParsedFunctionSignature.parse(tokener, errorLogger, null);

				tokener.required(T_AOPEN, "{ expected");

				List<ParsedStatement> statements = new ArrayList<ParsedStatement>();
				if (tokener.optional(T_ACLOSE) == null)
					while (tokener.optional(T_ACLOSE) == null) {
						statements.add(ParsedStatement.parse(tokener, errorLogger));
					}

				return new ParsedExpressionFunction(position, header, statements);

			case T_LT: {
				Token start = tokener.next();
				List<Token> tokens = new ArrayList<Token>();
				Token next = tokener.next();
				while (next.getType() != ZenLexer.T_GT) {
					tokens.add(next);
					next = tokener.next();
				}
				return new ParsedExpressionBracket(start.getPosition(), tokens);
			}
			case T_SQBROPEN: {
				tokener.next();
				List<ParsedExpression> contents = new ArrayList<ParsedExpression>();
				if (tokener.optional(T_SQBRCLOSE) == null)
					while (tokener.optional(T_SQBRCLOSE) == null) {
						contents.add(readAssignExpression(tokener, errorLogger));
						if (tokener.optional(T_COMMA) == null) {
							tokener.required(T_SQBRCLOSE, "] or , expected");
							break;
						}
					}
				return new ParsedExpressionArray(position, contents);
			}
			case T_AOPEN: {
				tokener.next();

				List<ParsedExpression> keys = new ArrayList<ParsedExpression>();
				List<ParsedExpression> values = new ArrayList<ParsedExpression>();
				if (tokener.optional(T_ACLOSE) == null) {
					while (tokener.optional(T_ACLOSE) == null) {
						keys.add(readAssignExpression(tokener, errorLogger));
						tokener.required(T_COLON, ": expected");
						values.add(readAssignExpression(tokener, errorLogger));

						if (tokener.optional(T_COMMA) == null) {
							tokener.required(T_ACLOSE, "} or , expected");
							break;
						}
					}

					return new ParsedExpressionAssociative(position, keys, values);
				}
			}
			case T_NEW: {
				Token newToken = tokener.next();
				IParsedType type = TypeParser.parse(tokener, errorLogger);
				ParsedCallArguments arguments = ParsedCallArguments.parse(tokener, errorLogger);
				return new ParsedExpressionNew(newToken.getPosition(), type, arguments);
			}
			case T_TRUE:
				tokener.next();
				return new ParsedExpressionBool(position, true);
			case T_FALSE:
				tokener.next();
				return new ParsedExpressionBool(position, false);
			case T_NULL:
				tokener.next();
				return new ParsedExpressionNull(position);
			case T_BROPEN:
				tokener.next();
				ParsedExpression result = readAssignExpression(tokener, errorLogger);
				tokener.required(T_BRCLOSE, ") expected");
				return result;
			default:
				Token last = tokener.next();
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

	public abstract IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType);

	public final Expression compile(IScopeMethod environment, ZenType predictedType)
	{
		return compilePartial(environment, predictedType).eval();
	}

	public String asIdentifier()
	{
		return null;
	}

	public Expression compileKey(IScopeMethod environment, ZenType predictedType)
	{
		return compile(environment, predictedType);
	}

	public abstract IAny eval(IZenCompileEnvironment environment);
}
