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
import stanhebben.zenscript.statements.StatementDoWhile;
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
public class ParsedStatementDoWhile extends ParsedStatement {
	public static ParsedStatementDoWhile parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_DO, "do expected").getPosition();
		
		String label = null;
		if (tokener.optional(T_COLON) != null) {
			label = tokener.required(TOKEN_ID, "identifier expected").getValue();
		}
		
		ParsedStatement contents = ParsedStatement.parse(tokener, errorLogger);
		
		tokener.required(T_WHILE, "while expected");
		ParsedExpression condition = ParsedExpression.parse(tokener, errorLogger);
		tokener.required(T_SEMICOLON, "; expected");
		
		return new ParsedStatementDoWhile(position, label, contents, condition);
	}
	
	private final String label;
	private final ParsedStatement contents;
	private final ParsedExpression condition;
	
	public ParsedStatementDoWhile(CodePosition position, String label, ParsedStatement contents, ParsedExpression condition) {
		super(position);
		
		this.label = label;
		this.contents = contents;
		this.condition = condition;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		Expression compiledCondition = condition.compile(scope, scope.getTypes().BOOL);
		StatementDoWhile compiled = new StatementDoWhile(getPosition(), scope, compiledCondition);
		
		ScopeStatementBlock statementScope = new ScopeStatementBlock(scope, compiled, label);
		compiled.setContents(contents.compile(statementScope));
		return compiled;
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
