/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.statement;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
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
public abstract class ParsedStatement {
	public static ParsedStatement parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		Token next = tokener.peek();
		switch (next.getType()) {
			case T_AOPEN:
				return ParsedStatementBlock.parse(tokener, errorLogger);
			
			case T_RETURN:
				return ParsedStatementReturn.parse(tokener, errorLogger);
			
			case T_VAR:
			case T_VAL:
				return ParsedStatementVar.parse(tokener, errorLogger);
			
			case T_IF: 
				return ParsedStatementIf.parse(tokener, errorLogger);
			
			case T_FOR:
				return ParsedStatementFor.parse(tokener, errorLogger);
			
			case T_SWITCH:
				return ParsedStatementSwitch.parse(tokener, errorLogger);
				
			case T_CASE:
				return ParsedStatementCase.parse(tokener, errorLogger);
				
			case T_DEFAULT:
				return ParsedStatementDefault.parse(tokener, errorLogger);
				
			case T_BREAK:
				return ParsedStatementBreak.parse(tokener, errorLogger);
				
			case T_CONTINUE:
				return ParsedStatementContinue.parse(tokener, errorLogger);
			
			case T_WHILE:
				return ParsedStatementWhile.parse(tokener, errorLogger);
			
			case T_DO:
				return ParsedStatementDoWhile.parse(tokener, errorLogger);
			
			default:
				return ParsedStatementExpression.parse(tokener, errorLogger);
		}
	}
	
	private final ZenPosition position;
	
	public ParsedStatement(ZenPosition position) {
		this.position = position;
	}
	
	public ZenPosition getPosition() {
		return position;
	}
	
	public abstract Statement compile(IScopeMethod scope);
	
	public abstract void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch);
}
