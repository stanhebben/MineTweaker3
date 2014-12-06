/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.statement;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedImportStatement extends ParsedStatement
{
	public static ParsedImportStatement parse(ZenLexer lexer, ICodeErrorLogger errorLogger)
	{
		CodePosition position = lexer.required(T_IMPORT, "import expected").getPosition();
		
		List<String> importNameElements = new ArrayList<String>();
		boolean wildcard = false;
		
		String nameStart = lexer.requiredIdentifier();
		importNameElements.add(nameStart);
		
		while (lexer.optional(T_DOT) != null) {
			if (lexer.optional(T_MUL) != null) {
				wildcard = true;
				break;
			}
			
			String namePart = lexer.requiredIdentifier();
			importNameElements.add(namePart);
		}

		String rename = null;
		if (!wildcard && lexer.optional(T_AS) != null)
			rename = lexer.requiredIdentifier();
		
		lexer.requiredSemicolon();

		return new ParsedImportStatement(position, importNameElements, rename, wildcard);
	}
	
	private final List<String> importName;
	private final String rename;
	private final boolean wildcard;
	
	public ParsedImportStatement(CodePosition position, List<String> importName, String rename, boolean wildcard)
	{
		super(position);
		
		this.importName = importName;
		this.rename = rename;
		this.wildcard = wildcard;
	}
	
	public List<String> getImportName()
	{
		return importName;
	}
	
	public String getRename()
	{
		return rename;
	}
	
	public boolean isWildcard()
	{
		return wildcard;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> Statement<E, T> compile(IScopeMethod<E, T> scope)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> void compileSwitch(IScopeMethod<E, T> scope, StatementSwitch<E, T> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
