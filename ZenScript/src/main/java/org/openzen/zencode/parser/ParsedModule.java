/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.parser.definition.IParsedDefinition;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.symbolic.ScriptBlock;
import org.openzen.zencode.symbolic.SymbolicModule;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.ScriptScope;

/**
 *
 * @author Stan
 */
public class ParsedModule
{
	private static final Charset UTF8 = Charset.forName("UTF-8");

	private final ICodeErrorLogger<?> errorLogger;
	private final IFileLoader fileLoader;
	private final String name;
	private final List<ParsedFile> files;

	public ParsedModule(
			ICodeErrorLogger<?> errorLogger,
			IFileLoader fileLoader,
			String name)
	{
		this.errorLogger = errorLogger;
		this.fileLoader = fileLoader;
		this.name = name;
		this.files = new ArrayList<ParsedFile>();
	}

	public ICodeErrorLogger<?> getErrorLogger()
	{
		return errorLogger;
	}

	public void addScript(String filename, String contents)
	{
		try {
			files.add(new ParsedFile(this, filename, new ZenLexer(errorLogger, contents)));
		} catch (IOException ex) {
			errorLogger.errorCannotLoadInclude(null, filename);
		}
	}

	public void addScript(File file)
	{
		try {
			Reader reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), UTF8);
			addScript(new ParsedFile(this, file.getName(), new ZenLexer(errorLogger, reader)));
		} catch (IOException ex) {
			errorLogger.errorCannotLoadInclude(null, name, ex);
		}
	}
	
	public void addScript(ParsedFile file)
	{
		files.add(file);
	}

	public InputStream loadFile(String name) throws IOException
	{
		return fileLoader == null ? null : fileLoader.load(name);
	}
	
	public List<ParsedFile> getFiles()
	{
		return files;
	}
	
	public <E extends IPartialExpression<E>> SymbolicModule<E> compileDefinitions(
			IZenCompileEnvironment<E> environment,
			IExpressionCompiler<E> compiler,
			TypeRegistry<E> typeRegistry)
	{
		SymbolicModule<E> symbolicModule = new SymbolicModule<E>(environment, compiler, typeRegistry);
		
		for (ParsedFile file : files) {
			IModuleScope<E> scriptScope = new ScriptScope<E>(symbolicModule.getScope());
			for (IParsedDefinition definition : file.getDefinitions()) {
				symbolicModule.addUnit(definition.compile(scriptScope));
			}
			
			if (!file.getStatements().isEmpty()) {
				ScriptBlock<E> scriptBlock = new ScriptBlock<E>(file.getFileName(), file.getStatements());
				symbolicModule.addScript(scriptBlock);
				
				for (ParsedStatement statement : scriptBlock.getSourceStatements()) {
					statement.processImports(scriptScope);
				}
			}
		}
		
		symbolicModule.compileDefinitions();
		return symbolicModule;
	}
}
