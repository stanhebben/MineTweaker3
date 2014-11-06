/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementSwitch;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public abstract class ParsedStatement {
	public static List<ParsedStatement> parseBlock(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		tokener.required(T_AOPEN, "{ expected");
		
		ArrayList<ParsedStatement> statements = new ArrayList<ParsedStatement>();
		while (tokener.optional(T_ACLOSE) == null) {
			statements.add(ParsedStatement.parse(tokener, errorLogger));
		}
		
		return statements;
	}
	
	public static ParsedStatement parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
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
	
	private final CodePosition position;
	
	public ParsedStatement(CodePosition position) {
		this.position = position;
	}
	
	public CodePosition getPosition() {
		return position;
	}
	
	public abstract Statement compile(IScopeMethod scope);
	
	public abstract void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch);
}
