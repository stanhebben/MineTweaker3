/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.statement;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.StatementBlockScope;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementForeach;
import org.openzen.zencode.symbolic.statement.StatementNull;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStatementFor extends ParsedStatement
{
	public static ParsedStatementFor parse(ZenLexer lexer)
	{
		CodePosition position = lexer.required(T_FOR, "for expected").getPosition();

		String name = lexer.required(TOKEN_ID, "identifier expected").getValue();
		List<String> names = new ArrayList<String>();
		names.add(name);

		while (lexer.optional(T_COMMA) != null) {
			names.add(lexer.required(TOKEN_ID, "identifier expected").getValue());
		}

		lexer.required(T_IN, "in expected");
		ParsedExpression source = ParsedExpression.parse(lexer);
		ParsedStatement content = ParsedStatement.parse(lexer);
		return new ParsedStatementFor(
				position,
				names,
				source,
				content);
	}

	private final List<String> names;
	private final ParsedExpression source;
	private final ParsedStatement content;

	public ParsedStatementFor(CodePosition position, List<String> names, ParsedExpression source, ParsedStatement content)
	{
		super(position);

		this.names = names;
		this.source = source;
		this.content = content;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 Statement<E, T> compile(IMethodScope<E, T> scope)
	{
		E compiledSource = source.compile(scope, null);
		List<T> iteratorTypes = compiledSource.getType().getIteratorTypes(names.size());
		if (iteratorTypes == null) {
			scope.getErrorLogger().errorNoSuchIterator(getPosition(), compiledSource.getType(), names.size());
			return new StatementNull<E, T>(getPosition(), scope);
		}

		List<SymbolLocal<E, T>> symbols = new ArrayList<SymbolLocal<E, T>>();
		StatementForeach<E, T> compiledStatement = new StatementForeach<E, T>(getPosition(), scope, symbols, compiledSource);
		StatementBlockScope<E, T> loopScope = new StatementBlockScope<E, T>(scope, compiledStatement, names);
		for (int i = 0; i < names.size(); i++) {
			SymbolLocal<E, T> symbol = new SymbolLocal<E, T>(iteratorTypes.get(i), true);
			symbols.add(symbol);
			loopScope.putValue(names.get(i), symbol, getPosition());
		}

		Statement<E, T> compiledContents = content.compile(loopScope);
		compiledStatement.setBody(compiledContents);

		return compiledStatement;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 void compileSwitch(IMethodScope<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
