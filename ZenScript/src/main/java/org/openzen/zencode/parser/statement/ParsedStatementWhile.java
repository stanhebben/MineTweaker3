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
import stanhebben.zenscript.statements.StatementWhile;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.TOKEN_ID;
import static org.openzen.zencode.lexer.ZenLexer.T_COLON;
import static org.openzen.zencode.lexer.ZenLexer.T_WHILE;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementWhile extends ParsedStatement {
	public static ParsedStatementWhile parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_WHILE, "while expected").getPosition();
		
		String label = null;
		if (tokener.optional(T_COLON) != null) {
			label = tokener.required(TOKEN_ID, "identifier expected").getValue();
		}
		
		ParsedExpression condition = ParsedExpression.parse(tokener, errorLogger);
		ParsedStatement contents = ParsedStatement.parse(tokener, errorLogger);
		
		return new ParsedStatementWhile(position, label, condition, contents);
	}
	
	private final String label;
	private final ParsedExpression condition;
	private final ParsedStatement contents;
	
	public ParsedStatementWhile(CodePosition position, String label, ParsedExpression condition, ParsedStatement contents) {
		super(position);
		
		this.label = label;
		this.condition = condition;
		this.contents = contents;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		Expression cCondition = condition.compile(scope, scope.getTypes().BOOL);
		StatementWhile result = new StatementWhile(getPosition(), scope, cCondition);
		ScopeStatementBlock blockScope = new ScopeStatementBlock(scope, result, label);
		result.setContents(contents.compile(blockScope));
		return result;
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
