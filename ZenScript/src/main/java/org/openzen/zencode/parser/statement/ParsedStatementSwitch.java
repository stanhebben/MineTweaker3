/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.scope.ScopeStatementBlock;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.statements.Statement;
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
public class ParsedStatementSwitch extends ParsedStatement {
	public static ParsedStatementSwitch parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_SWITCH, "switch expected").getPosition();
		
		String label = null;
		if (tokener.optional(T_COLON) != null) {
			label = tokener.required(TOKEN_ID, "identifier expected").getValue();
		}
		
		ParsedExpression value = ParsedExpression.parse(tokener, errorLogger);
		ParsedStatementBlock contents = ParsedStatementBlock.parse(tokener, errorLogger);
		
		return new ParsedStatementSwitch(position, label, value, contents);
	}
	
	private final String label;
	private final ParsedExpression value;
	private final ParsedStatementBlock contents;
	
	public ParsedStatementSwitch(
			CodePosition position,
			String label,
			ParsedExpression value,
			ParsedStatementBlock contents) {
		super(position);
		
		this.label = label;
		this.value = value;
		this.contents = contents;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		Expression cValue = value.compile(scope, null);
		StatementSwitch forSwitch = new StatementSwitch(getPosition(), scope, cValue);
		
		IScopeMethod switchScope = new ScopeStatementBlock(scope, forSwitch, label);
		contents.compileSwitch(switchScope, forSwitch);
		
		return forSwitch;
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
