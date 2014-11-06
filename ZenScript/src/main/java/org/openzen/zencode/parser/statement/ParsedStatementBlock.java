/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.statement;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.ScopeStatementBlock;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementBlock;
import stanhebben.zenscript.statements.StatementSwitch;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.T_ACLOSE;
import static org.openzen.zencode.lexer.ZenLexer.T_AOPEN;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementBlock extends ParsedStatement {
	public static ParsedStatementBlock parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_AOPEN, "{ expected").getPosition();
		
		ArrayList<ParsedStatement> statements = new ArrayList<ParsedStatement>();
		while (tokener.optional(T_ACLOSE) == null) {
			statements.add(ParsedStatement.parse(tokener, errorLogger));
		}
		
		return new ParsedStatementBlock(position, statements);
	}
	
	private final List<ParsedStatement> statements;
	
	public ParsedStatementBlock(CodePosition position, List<ParsedStatement> statements) {
		super(position);
		
		this.statements = statements;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		IScopeMethod blockScope = new ScopeStatementBlock(scope);
		
		List<Statement> result = new ArrayList<Statement>();
		for (ParsedStatement statement : statements) {
			result.add(statement.compile(blockScope));
		}
		
		return new StatementBlock(getPosition(), scope, result);
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
