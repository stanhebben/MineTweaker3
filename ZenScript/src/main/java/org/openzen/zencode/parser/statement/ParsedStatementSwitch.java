/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.StatementBlockScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementSwitch extends ParsedStatement
{
	public static ParsedStatementSwitch parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_SWITCH, "switch expected").getPosition();

		String label = null;
		if (lexer.optional(T_COLON) != null)
			label = lexer.required(TOKEN_ID, "identifier expected").getValue();

		ParsedExpression value = ParsedExpression.parse(lexer);
		ParsedStatementBlock contents = ParsedStatementBlock.parse(lexer);

		return new ParsedStatementSwitch(position, label, value, contents);
	}

	private final String label;
	private final ParsedExpression value;
	private final ParsedStatementBlock contents;

	public ParsedStatementSwitch(
			CodePosition position,
			String label,
			ParsedExpression value,
			ParsedStatementBlock contents)
	{
		super(position);

		this.label = label;
		this.value = value;
		this.contents = contents;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 Statement<E, T> compile(IMethodScope<E, T> scope)
	{
		E cValue = value.compile(scope, null);
		StatementSwitch<E, T> forSwitch = new StatementSwitch<E, T>(getPosition(), scope, cValue);

		IMethodScope<E, T> switchScope = new StatementBlockScope<E, T>(scope, forSwitch, label);
		contents.compileSwitch(switchScope, forSwitch);

		return forSwitch;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 void compileSwitch(IMethodScope<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
