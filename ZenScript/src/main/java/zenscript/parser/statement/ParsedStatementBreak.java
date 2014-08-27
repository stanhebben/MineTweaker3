/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.statement;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementBreak;
import stanhebben.zenscript.statements.StatementNull;
import stanhebben.zenscript.statements.StatementSwitch;
import zenscript.IZenErrorLogger;
import zenscript.lexer.Token;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.*;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementBreak extends ParsedStatement {
	public static ParsedStatementBreak parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		ZenPosition position = tokener.required(T_BREAK, "break expected").getPosition();
		
		Token label = tokener.optional(TOKEN_ID);
		tokener.required(T_SEMICOLON, "; expected");
		
		return new ParsedStatementBreak(position, label == null ? null : label.getValue());
	}
	
	private final String label;
	
	public ParsedStatementBreak(ZenPosition position, String label) {
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
			return new StatementBreak(getPosition(), scope, controlStatement);
		}
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
