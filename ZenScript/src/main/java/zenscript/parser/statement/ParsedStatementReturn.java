/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.statement;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementReturn;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.T_RETURN;
import static zenscript.lexer.ZenTokener.T_SEMICOLON;
import zenscript.parser.expression.ParsedExpression;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementReturn extends ParsedStatement {
	public static ParsedStatementReturn parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		ZenPosition position = tokener.required(T_RETURN, "return expected").getPosition();
		
		ParsedExpression expression = null;
		if (tokener.optional(T_SEMICOLON) == null) {
			expression = ParsedExpression.parse(tokener, errorLogger);
			tokener.required(T_SEMICOLON, "; expected");
		}
		
		return new ParsedStatementReturn(position, expression);
	}
	
	private final ParsedExpression value;
	
	public ParsedStatementReturn(ZenPosition position, ParsedExpression value) {
		super(position);
		
		this.value = value;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		return new StatementReturn(
				getPosition(),
				scope,
				value == null ? null : value.compile(scope, scope.getReturnType()).eval());
	}
}
