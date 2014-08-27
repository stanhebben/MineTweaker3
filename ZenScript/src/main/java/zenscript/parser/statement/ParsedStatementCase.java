/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.statement;

import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.*;
import zenscript.parser.expression.ParsedExpression;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementCase extends ParsedStatement {
	public static ParsedStatementCase parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		ZenPosition position = tokener.required(T_CASE, "case expected").getPosition();
		
		ParsedExpression value = ParsedExpression.parse(tokener, errorLogger);
		tokener.required(T_COLON, ": expected");
		
		return new ParsedStatementCase(position, value);
	}
	
	private final ParsedExpression value;
	
	public ParsedStatementCase(ZenPosition position, ParsedExpression value) {
		super(position);
		
		this.value = value;
	}
}
