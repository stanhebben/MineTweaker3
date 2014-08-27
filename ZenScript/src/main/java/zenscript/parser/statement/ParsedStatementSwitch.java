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
public class ParsedStatementSwitch extends ParsedStatement {
	public static ParsedStatementSwitch parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		ZenPosition position = tokener.required(T_SWITCH, "switch expected").getPosition();
		
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
			ZenPosition position,
			String label,
			ParsedExpression value,
			ParsedStatementBlock contents) {
		super(position);
		
		this.label = label;
		this.value = value;
		this.contents = contents;
	}
}
