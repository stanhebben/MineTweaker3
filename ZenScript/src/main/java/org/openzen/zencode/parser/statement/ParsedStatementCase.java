/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementNull;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementCase extends ParsedStatement
{
	public static ParsedStatementCase parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_CASE, "case expected").getPosition();

		ParsedExpression value = ParsedExpression.parse(lexer);
		lexer.required(T_COLON, ": expected");

		return new ParsedStatementCase(position, value);
	}

	private final ParsedExpression value;

	public ParsedStatementCase(CodePosition position, ParsedExpression value)
	{
		super(position);

		this.value = value;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		 Statement<E, T> compile(IMethodScope<E, T> scope)
	{
		scope.getErrorLogger().errorCaseOutsideSwitch(getPosition());
		return new StatementNull<E, T>(getPosition(), scope);
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		 void compileSwitch(IMethodScope<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onCase(getPosition(), value.compile(scope, forSwitch.getType()));
	}
}
