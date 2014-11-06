/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementContinue;
import stanhebben.zenscript.statements.StatementNull;
import stanhebben.zenscript.statements.StatementSwitch;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementContinue extends ParsedStatement {
	public static ParsedStatementContinue parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_CONTINUE, "continue expected").getPosition();
		
		Token label = tokener.optional(TOKEN_ID);
		tokener.required(T_SEMICOLON, "; expected");
		
		return new ParsedStatementContinue(position, label == null ? null : label.getValue());
	}
	
	private final String label;
	
	public ParsedStatementContinue(CodePosition position, String label) {
		super(position);
		
		this.label = label;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		Statement controlStatement = scope.getControlStatement(label);
		
		if (controlStatement == null) {
			if (label == null) {
				scope.error(getPosition(), "Not inside a control statement");
			} else {
				scope.error(getPosition(), "No control statement with the label " + label);
			}
			return new StatementNull(getPosition(), scope);
		} else {
			return new StatementContinue(getPosition(), scope, controlStatement);
		}
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
