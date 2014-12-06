/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.ScopeStatementBlock;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementIf;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementIf extends ParsedStatement
{
	public static ParsedStatementIf parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_IF, "if expected").getPosition();

		ParsedExpression condition = ParsedExpression.parse(lexer);
		ParsedStatement onIf = ParsedStatement.parse(lexer);
		ParsedStatement onElse = null;
		if (lexer.optional(T_ELSE) != null)
			onElse = ParsedStatement.parse(lexer);

		return new ParsedStatementIf(position, condition, onIf, onElse);
	}

	private final ParsedExpression condition;
	private final ParsedStatement onIf;
	private final ParsedStatement onElse;

	public ParsedStatementIf(CodePosition position, ParsedExpression condition, ParsedStatement onIf, ParsedStatement onElse)
	{
		super(position);

		this.condition = condition;
		this.onIf = onIf;
		this.onElse = onElse;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 Statement<E, T> compile(IScopeMethod<E, T> scope)
	{
		IAny eval = condition.eval(scope.getEnvironment());
		if (eval != null)
			// compile-time variable
			if (eval.asBool()) {
				ScopeStatementBlock<E, T> ifScope = new ScopeStatementBlock<E, T>(scope);
				return onIf.compile(ifScope);
			} else {
				ScopeStatementBlock<E, T> elseScope = new ScopeStatementBlock<E, T>(scope);
				return onElse.compile(elseScope);
			}
		else {
			// runtime variable
			E compiledCondition = condition.compile(scope, scope.getTypes().getBool());
			ScopeStatementBlock<E, T> ifScope = new ScopeStatementBlock<E, T>(scope);
			Statement<E, T> compiledIf = onIf.compile(ifScope);
			Statement<E, T> compiledElse = null;

			if (onElse != null) {
				ScopeStatementBlock<E, T> elseScope = new ScopeStatementBlock<E, T>(scope);
				compiledElse = onElse.compile(elseScope);
			}

			return new StatementIf<E, T>(getPosition(), scope, compiledCondition, compiledIf, compiledElse);
		}
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 void compileSwitch(IScopeMethod<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
