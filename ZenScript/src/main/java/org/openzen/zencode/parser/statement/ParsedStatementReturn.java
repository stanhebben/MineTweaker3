/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementReturn;
import stanhebben.zenscript.statements.StatementSwitch;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_RETURN;
import static org.openzen.zencode.lexer.ZenLexer.T_SEMICOLON;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementReturn extends ParsedStatement {
	public static ParsedStatementReturn parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_RETURN, "return expected").getPosition();
		
		ParsedExpression expression = null;
		if (tokener.optional(T_SEMICOLON) == null) {
			expression = ParsedExpression.parse(tokener, errorLogger);
			tokener.required(T_SEMICOLON, "; expected");
		}
		
		return new ParsedStatementReturn(position, expression);
	}
	
	private final ParsedExpression value;
	
	public ParsedStatementReturn(CodePosition position, ParsedExpression value) {
		super(position);
		
		this.value = value;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		return new StatementReturn(
				getPosition(),
				scope,
				value == null ? null : value.compile(scope, scope.getReturnType()));
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
