/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser;

import org.openzen.zencode.java.JavaCompiler;
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
import org.openzen.zencode.symbolic.SymbolicModule;

/**
 *
 * @author Stan
 */
public class ParsedModule
{
	private static final Charset UTF8 = Charset.forName("UTF-8");

	private final JavaCompiler global;
	private final IFileLoader fileLoader;
	private final String name;
	private final List<ParsedFile> files;

	public ParsedModule(JavaCompiler global, IFileLoader fileLoader, String name)
	{
		this.global = global;
		this.fileLoader = fileLoader;
		this.name = name;
		this.files = new ArrayList<ParsedFile>();
	}

	public JavaCompiler getParserEnvironment()
	{
		return global;
	}

	public ICodeErrorLogger getErrorLogger()
	{
		return global.getCompileEnvironment().getErrorLogger();
	}

	public void addScript(String filename, String contents)
	{
		try {
			files.add(new ParsedFile(this, filename, new ZenLexer(global.getCompileEnvironment().getErrorLogger(), contents)));
		} catch (IOException ex) {
			global.getCompileEnvironment().getErrorLogger().error(
					null,
					"Could not load " + filename + ": " + ex.getMessage());
		}
	}

	public void addScript(File file)
	{
		try {
			Reader reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), UTF8);
			files.add(new ParsedFile(this, file.getName(), new ZenLexer(global.getCompileEnvironment().getErrorLogger(), reader)));
		} catch (IOException ex) {
			global.getCompileEnvironment().getErrorLogger().error(
					null,
					"Could not load " + file.getName() + ": " + ex.getMessage());
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
	
	public void compileUnits(SymbolicModule module)
	{
		for (ParsedFile file : files) {
			file.compileUnits(module);
		}
	}
	
	public void compileFunctions(SymbolicModule module)
	{
		for (ParsedFile file : files) {
			file.compileFunctions(module);
		}
	}
	
	public void compileContents(SymbolicModule module)
	{
		for (ParsedFile file : files) {
			file.compileContents(module);
		}
	}
}
