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
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.parser.unit.IParsedDefinition;
import org.openzen.zencode.symbolic.ScriptBlock;
import org.openzen.zencode.symbolic.SymbolicModule;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IGlobalScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 */
public class ParsedModule
{
	private static final Charset UTF8 = Charset.forName("UTF-8");

	private final ICodeErrorLogger<?, ?> errorLogger;
	private final IFileLoader fileLoader;
	private final String name;
	private final List<ParsedFile> files;

	public ParsedModule(
			ICodeErrorLogger<?, ?> errorLogger,
			IFileLoader fileLoader,
			String name)
	{
		this.errorLogger = errorLogger;
		this.fileLoader = fileLoader;
		this.name = name;
		this.files = new ArrayList<ParsedFile>();
	}

	public ICodeErrorLogger<?, ?> getErrorLogger()
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
			files.add(new ParsedFile(this, file.getName(), new ZenLexer(errorLogger, reader)));
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
	
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> SymbolicModule<E, T> compileDefinitions(IGlobalScope<E, T> global)
	{
		SymbolicModule<E, T> symbolicModule = new SymbolicModule<E, T>(global);
		
		for (ParsedFile file : files) {
			for (IParsedDefinition definition : file.getDefinitions()) {
				symbolicModule.addUnit(definition.compile(symbolicModule.getScope()));
			}
			
			if (!file.getStatements().isEmpty()) {
				symbolicModule.addScript(new ScriptBlock<E, T>(file.getFileName(), file.getStatements()));
			}
		}
		
		symbolicModule.compileDefinitions();
		return symbolicModule;
	}
}
