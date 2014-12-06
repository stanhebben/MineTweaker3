/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementReturn;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_RETURN;
import static org.openzen.zencode.lexer.ZenLexer.T_SEMICOLON;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementReturn extends ParsedStatement
{
	public static ParsedStatementReturn parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_RETURN, "return expected").getPosition();

		ParsedExpression expression = null;
		if (lexer.optional(T_SEMICOLON) == null) {
			expression = ParsedExpression.parse(lexer);
			lexer.required(T_SEMICOLON, "; expected");
		}

		return new ParsedStatementReturn(position, expression);
	}

	private final ParsedExpression value;

	public ParsedStatementReturn(CodePosition position, ParsedExpression value)
	{
		super(position);

		this.value = value;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		Statement<E, T> compile(IScopeMethod<E, T> scope)
	{
		return new StatementReturn<E, T>(
				getPosition(),
				scope,
				value == null ? null : value.compile(scope, scope.getReturnType()));
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 void compileSwitch(IScopeMethod<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
