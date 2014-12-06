/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.scope.ScopeStatementBlock;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.symbolic.statement.StatementWhile;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.TOKEN_ID;
import static org.openzen.zencode.lexer.ZenLexer.T_COLON;
import static org.openzen.zencode.lexer.ZenLexer.T_WHILE;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementWhile extends ParsedStatement
{
	public static ParsedStatementWhile parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_WHILE, "while expected").getPosition();

		String label = null;
		if (lexer.optional(T_COLON) != null)
			label = lexer.required(TOKEN_ID, "identifier expected").getValue();

		ParsedExpression condition = ParsedExpression.parse(lexer);
		ParsedStatement contents = ParsedStatement.parse(lexer);

		return new ParsedStatementWhile(position, label, condition, contents);
	}

	private final String label;
	private final ParsedExpression condition;
	private final ParsedStatement contents;

	public ParsedStatementWhile(CodePosition position, String label, ParsedExpression condition, ParsedStatement contents)
	{
		super(position);

		this.label = label;
		this.condition = condition;
		this.contents = contents;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 Statement<E, T> compile(IScopeMethod<E, T> scope)
	{
		E cCondition = condition.compile(scope, scope.getTypes().getBool());
		StatementWhile<E, T> result = new StatementWhile<E, T>(getPosition(), scope, cCondition);
		ScopeStatementBlock<E, T> blockScope = new ScopeStatementBlock<E, T>(scope, result, label);
		result.setContents(contents.compile(blockScope));
		return result;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 void compileSwitch(IScopeMethod<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
