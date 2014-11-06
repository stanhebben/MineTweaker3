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
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementDefault extends ParsedStatement {
	public static ParsedStatementDefault parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_DEFAULT, "default expected").getPosition();
		tokener.required(T_COLON, ": expected");
		
		return new ParsedStatementDefault(position);
	}
	
	public ParsedStatementDefault(CodePosition position) {
		super(position);
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		scope.error(getPosition(), "default statement can only be used inside a switch");
		return new StatementNull(getPosition(), scope);
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onDefault(getPosition());
	}
}
