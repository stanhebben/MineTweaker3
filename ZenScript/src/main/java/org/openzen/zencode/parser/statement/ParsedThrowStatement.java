/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_THROW;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.symbolic.statement.ThrowStatement;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedThrowStatement extends ParsedStatement
{
	public static ParsedThrowStatement parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_THROW, "throw expected").getPosition();
		ParsedExpression expression = ParsedExpression.parse(lexer);
		return new ParsedThrowStatement(position, expression);
	}
	
	private final ParsedExpression expression;
	
	public ParsedThrowStatement(CodePosition position, ParsedExpression expression)
	{
		super(position);
		
		this.expression = expression;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> Statement<E, T> compile(IMethodScope<E, T> scope)
	{
		return new ThrowStatement<E, T>(getPosition(), scope, expression.compile(scope, null));
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> void compileSwitch(IMethodScope<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
