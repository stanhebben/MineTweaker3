/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.statement;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.compiler.ScopeStatementBlock;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementBlock;
import stanhebben.zenscript.statements.StatementSwitch;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.T_ACLOSE;
import static zenscript.lexer.ZenTokener.T_AOPEN;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementBlock extends ParsedStatement {
	public static ParsedStatementBlock parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		ZenPosition position = tokener.required(T_AOPEN, "{ expected").getPosition();
		
		ArrayList<ParsedStatement> statements = new ArrayList<ParsedStatement>();
		while (tokener.optional(T_ACLOSE) == null) {
			statements.add(ParsedStatement.parse(tokener, errorLogger));
		}
		
		return new ParsedStatementBlock(position, statements);
	}
	
	private final List<ParsedStatement> statements;
	
	public ParsedStatementBlock(ZenPosition position, List<ParsedStatement> statements) {
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
