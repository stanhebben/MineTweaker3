/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementNull;
import stanhebben.zenscript.statements.StatementSwitch;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementCase extends ParsedStatement {
	public static ParsedStatementCase parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_CASE, "case expected").getPosition();
		
		ParsedExpression value = ParsedExpression.parse(tokener, errorLogger);
		tokener.required(T_COLON, ": expected");
		
		return new ParsedStatementCase(position, value);
	}
	
	private final ParsedExpression value;
	
	public ParsedStatementCase(CodePosition position, ParsedExpression value) {
		super(position);
		
		this.value = value;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		scope.error(getPosition(), "case must be inside a switch");
		return new StatementNull(getPosition(), scope);
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onCase(getPosition(), value.compile(scope, forSwitch.getType()));
	}
}
