/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import org.openzen.zencode.symbolic.scope.ScopeStatementBlock;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementIf;
import stanhebben.zenscript.statements.StatementSwitch;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementIf extends ParsedStatement {
	public static ParsedStatementIf parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_IF, "if expected").getPosition();
		
		ParsedExpression condition = ParsedExpression.parse(tokener, errorLogger);
		ParsedStatement onIf = ParsedStatement.parse(tokener, errorLogger);
		ParsedStatement onElse = null;
		if (tokener.optional(T_ELSE) != null) {
			onElse = ParsedStatement.parse(tokener, errorLogger);
		}
		
		return new ParsedStatementIf(position, condition, onIf, onElse);
	}
	
	private final ParsedExpression condition;
	private final ParsedStatement onIf;
	private final ParsedStatement onElse;
	
	public ParsedStatementIf(CodePosition position, ParsedExpression condition, ParsedStatement onIf, ParsedStatement onElse) {
		super(position);
		
		this.condition = condition;
		this.onIf = onIf;
		this.onElse = onElse;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		IAny eval = condition.eval(scope.getEnvironment());
		if (eval != null) {
			// compilePartial-time variable
			if (eval.asBool()) {
				ScopeStatementBlock ifScope = new ScopeStatementBlock(scope);
				return onIf.compile(ifScope);
			} else {
				ScopeStatementBlock elseScope = new ScopeStatementBlock(scope);
				return onElse.compile(elseScope);
			}
		} else {
			// runtime variable
			Expression compiledCondition = condition.compile(scope, scope.getTypes().BOOL);
			ScopeStatementBlock ifScope = new ScopeStatementBlock(scope);
			Statement compiledIf = onIf.compile(ifScope);
			Statement compiledElse = null;
			
			if (onElse != null) {
				ScopeStatementBlock elseScope = new ScopeStatementBlock(scope);
				compiledElse = onElse.compile(elseScope);
			}
			
			return new StatementIf(getPosition(), scope, compiledCondition, compiledIf, compiledElse);
		}
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
