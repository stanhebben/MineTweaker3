/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.symbolic.statement.StatementVar;
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.lexer.ParseException;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementVar extends ParsedStatement
{
	public static ParsedStatementVar parse(ZenLexer lexer)
	{
		Token token = lexer.next();
		if (token.getType() != T_VAR && token.getType() != T_VAL)
			throw new ParseException(token, "var or val expected");

		String name = lexer.required(TOKEN_ID, "identifier expected").getValue();

		IParsedType type = null;
		ParsedExpression initializer = null;
		if (lexer.optional(T_AS) != null)
			type = TypeParser.parse(lexer);

		if (lexer.optional(T_ASSIGN) != null)
			initializer = ParsedExpression.parse(lexer);

		lexer.required(T_SEMICOLON, "; expected");
		return new ParsedStatementVar(token.getPosition(), name, type, initializer, token.getType() == T_VAL);
	}

	private final String name;
	private final boolean isFinal;
	private final IParsedType asType;
	private final ParsedExpression initializer;

	public ParsedStatementVar(CodePosition position, String name, IParsedType asType, ParsedExpression initializer, boolean isFinal)
	{
		super(position);

		this.name = name;
		this.asType = asType;
		this.initializer = initializer;
		this.isFinal = isFinal;
	}

	@Override
	public <E extends IPartialExpression<E>>
		 StatementVar<E> compile(IMethodScope<E> scope)
	{
		IGenericType<E> cType = scope.getTypeCompiler().any;
		E cInitializer = null;
		if (asType != null)
			cType = asType.compile(scope);

		LocalSymbol<E> symbol = new LocalSymbol<E>(cType, isFinal);
		scope.putValue(name, symbol, getPosition());
		return new StatementVar<E>(getPosition(), scope, symbol, cInitializer);
	}

	@Override
	public <E extends IPartialExpression<E>>
		 void compileSwitch(IMethodScope<E> scope, StatementSwitch<E> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
