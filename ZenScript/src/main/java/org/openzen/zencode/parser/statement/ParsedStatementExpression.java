/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementExpression;
import stanhebben.zenscript.statements.StatementSwitch;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_SEMICOLON;
import org.openzen.zencode.parser.expression.ParsedExpression;

/**
 *
 * @author Stan
 */
public class ParsedStatementExpression extends ParsedStatement {
	public static ParsedStatementExpression parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		ParsedExpression expression = ParsedExpression.parse(tokener, errorLogger);
		tokener.required(T_SEMICOLON, "; expected");
		
		return new ParsedStatementExpression(expression);
	}
	
	private final ParsedExpression expression;
	
	private ParsedStatementExpression(ParsedExpression expression) {
		super(expression.getPosition());
		
		this.expression = expression;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		return new StatementExpression(getPosition(), scope, expression.compile(scope, null));
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
