/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.statement;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementExpression;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.T_SEMICOLON;
import zenscript.parser.expression.ParsedExpression;

/**
 *
 * @author Stan
 */
public class ParsedStatementExpression extends ParsedStatement {
	public static ParsedStatementExpression parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		ParsedExpression expression = ParsedExpression.parse(tokener, errorLogger);
		tokener.required(T_SEMICOLON, "; expected");
		
		return new ParsedStatementExpression(expression);
	}
	
	private final ParsedExpression expression;
	
	private ParsedStatementExpression(ParsedExpression expression) {
		super(expression.getPosition());
		
		this.expression = expression;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		return new StatementExpression(getPosition(), scope, expression.compile(scope, null).eval());
	}
}
