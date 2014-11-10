/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.elements;

import java.util.List;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.symbolic.scope.IScopeModule;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedFunction
{
	public static ParsedFunction parse(ZenLexer tokener, ICodeErrorLogger errorLogger)
	{
		tokener.next();

		List<ParsedGenericParameter> genericParameters
				= ParsedGenericParameters.parse(tokener, errorLogger);
		CodePosition position = tokener.getPosition();
		String name = tokener.requiredIdentifier();

		ParsedFunctionSignature header
				= ParsedFunctionSignature.parse(tokener, errorLogger, genericParameters);
		List<ParsedStatement> statements
				= ParsedStatement.parseBlock(tokener, errorLogger);

		return new ParsedFunction(position, name, header, statements);
	}

	private final CodePosition position;
	private final String name;
	private final ParsedFunctionSignature header;
	private final List<ParsedStatement> statements;

	private SymbolicFunction compiled;

	private ParsedFunction(CodePosition position, String name, ParsedFunctionSignature header, List<ParsedStatement> statements)
	{
		this.position = position;
		this.name = name;
		this.header = header;
		this.statements = statements;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public String getName()
	{
		return name;
	}

	public ParsedFunctionSignature getHeader()
	{
		return header;
	}

	public List<ParsedStatement> getStatements()
	{
		return statements;
	}

	public SymbolicFunction compileHeader(IScopeModule scope)
	{
		compiled = new SymbolicFunction(position, header.compile(scope), scope);
		return compiled;
	}

	public void compileContents()
	{
		for (ParsedStatement statement : statements) {
			compiled.addStatement(statement.compile(compiled.getScope()));
		}
	}
}
