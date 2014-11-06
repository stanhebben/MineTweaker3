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
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementForeach;
import stanhebben.zenscript.statements.StatementNull;
import stanhebben.zenscript.statements.StatementSwitch;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.IZenIterator;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementFor extends ParsedStatement {
	public static ParsedStatementFor parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_FOR, "for expected").getPosition();
		
		String name = tokener.required(TOKEN_ID, "identifier expected").getValue();
		List<String> names = new ArrayList<String>();
		names.add(name);

		while (tokener.optional(T_COMMA) != null) {
			names.add(tokener.required(TOKEN_ID, "identifier expected").getValue());
		}
		
		tokener.required(T_IN, "in expected");
		ParsedExpression source = ParsedExpression.parse(tokener, errorLogger);
		ParsedStatement content = ParsedStatement.parse(tokener, errorLogger);
		return new ParsedStatementFor(
				position,
				names,
				source,
				content);
	}
	
	private final List<String> names;
	private final ParsedExpression source;
	private final ParsedStatement content;
	
	public ParsedStatementFor(CodePosition position, List<String> names, ParsedExpression source, ParsedStatement content) {
		super(position);
		
		this.names = names;
		this.source = source;
		this.content = content;
	}

	@Override
	public Statement compile(IScopeMethod scope) {
		Expression compiledSource = source.compile(scope, null);
		IZenIterator iterator = compiledSource.getType().makeIterator(names.size());
		if (iterator == null) {
			scope.error(getPosition(), compiledSource.getType().getName() + " has no iterator with " + names.size() + " variables.");
			return new StatementNull(getPosition(), scope);
		}
		
		
		List<SymbolLocal> symbols = new ArrayList<SymbolLocal>();
		StatementForeach compiledStatement = new StatementForeach(getPosition(), scope, symbols, compiledSource, iterator);
		ScopeStatementBlock loopScope = new ScopeStatementBlock(scope, compiledStatement, names);
		for (int i = 0; i < names.size(); i++) {
			SymbolLocal symbol = new SymbolLocal(iterator.getType(i), true);
			symbols.add(symbol);
			loopScope.putValue(names.get(i), symbol, getPosition());
		}
		
		Statement compiledContents = content.compile(loopScope);
		compiledStatement.setBody(compiledContents);
		
		return compiledStatement;
	}

	@Override
	public void compileSwitch(IScopeMethod scope, StatementSwitch forSwitch) {
		forSwitch.onStatement(compile(scope));
	}
}
