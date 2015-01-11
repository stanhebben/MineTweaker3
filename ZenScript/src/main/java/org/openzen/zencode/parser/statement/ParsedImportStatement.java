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
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.statement.ImportStatement;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementSwitch;
import org.openzen.zencode.symbolic.symbols.ImportableSymbol;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedImportStatement extends ParsedStatement
{
	public static ParsedImportStatement parse(ZenLexer lexer, ICodeErrorLogger<?> errorLogger)
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
	public <E extends IPartialExpression<E>> void processImports(IModuleScope<E> scope)
	{
		IImportable<E> importable = scope.getRootPackage().resolve(getPosition(), scope.getErrorLogger(), importName, wildcard);
		if (wildcard) {
			for (String definitionName : importable.getSubDefinitionNames()) {
				scope.putImport(definitionName, new ImportableSymbol<E>(importable), getPosition());
			}
		} else {
			scope.putImport(
					rename == null ? importName.get(importName.size() - 1) : rename,
					new ImportableSymbol<E>(importable), getPosition());
		}
	}

	@Override
	public <E extends IPartialExpression<E>> Statement<E> compile(IMethodScope<E> scope)
	{
		return new ImportStatement<E>(getPosition(), scope, importName, rename, wildcard);
	}

	@Override
	public <E extends IPartialExpression<E>> void compileSwitch(IMethodScope<E> scope, StatementSwitch<E> forSwitch)
	{
		forSwitch.onStatement(compile(scope));
	}
}
