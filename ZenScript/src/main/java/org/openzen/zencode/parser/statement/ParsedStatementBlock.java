/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.ScopeStatementBlock;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementBlock;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_ACLOSE;
import static org.openzen.zencode.lexer.ZenLexer.T_AOPEN;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementBlock extends ParsedStatement
{
	public static ParsedStatementBlock parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_AOPEN, "{ expected").getPosition();

		ArrayList<ParsedStatement> statements = new ArrayList<ParsedStatement>();
		while (lexer.optional(T_ACLOSE) == null) {
			statements.add(ParsedStatement.parse(lexer));
		}

		return new ParsedStatementBlock(position, statements);
	}

	private final List<ParsedStatement> statements;

	public ParsedStatementBlock(CodePosition position, List<ParsedStatement> statements)
	{
		super(position);

		this.statements = statements;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 Statement<E, T> compile(IScopeMethod<E, T> scope)
	{
		IScopeMethod<E, T> blockScope = new ScopeStatementBlock<E, T>(scope);

		List<Statement<E, T>> result = new ArrayList<Statement<E, T>>();
		for (ParsedStatement statement : statements) {
			result.add(statement.compile(blockScope));
		}

		return new StatementBlock<E, T>(getPosition(), scope, result);
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 void compileSwitch(IScopeMethod<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
