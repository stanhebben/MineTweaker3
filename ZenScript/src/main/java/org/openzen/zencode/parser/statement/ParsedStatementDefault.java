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
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementDefault extends ParsedStatement
{
	public static ParsedStatementDefault parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_DEFAULT, "default expected").getPosition();
		lexer.required(T_COLON, ": expected");

		return new ParsedStatementDefault(position);
	}

	public ParsedStatementDefault(CodePosition position)
	{
		super(position);
	}

	@Override
	public <E extends IPartialExpression<E>> Statement<E> compile(IMethodScope<E> scope)
	{
		scope.getErrorLogger().errorDefaultOutsideSwitch(getPosition());
		return new StatementNull<E>(getPosition(), scope);
	}

	@Override
	public <E extends IPartialExpression<E>>
		 void compileSwitch(IMethodScope<E> scope, StatementSwitch<E> forSwitch)
	{
		forSwitch.onDefault(getPosition());
	}
}
