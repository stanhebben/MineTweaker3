/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementExpression;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_SEMICOLON;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 */
public class ParsedStatementExpression extends ParsedStatement
{
	public static ParsedStatementExpression parse(ZenLexer lexer)
	{
		ParsedExpression expression = ParsedExpression.parse(lexer);
		lexer.required(T_SEMICOLON, "; expected");

		return new ParsedStatementExpression(expression);
	}

	private final ParsedExpression expression;

	private ParsedStatementExpression(ParsedExpression expression)
	{
		super(expression.getPosition());

		this.expression = expression;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
			Statement<E, T> compile(IScopeMethod<E, T> scope)
	{
		return new StatementExpression<E, T>(getPosition(), scope, expression.compile(scope, null));
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 void compileSwitch(IScopeMethod<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
