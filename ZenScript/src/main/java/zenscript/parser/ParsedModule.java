/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser;

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
import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;

/**
 *
 * @author Stan
 */
public class ParsedModule {
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	private final ParserEnvironment global;
	private final IFileLoader fileLoader;
	private final String name;
	private final List<ParsedFile> files;
	
	public ParsedModule(ParserEnvironment global, IFileLoader fileLoader, String name) {
		this.global = global;
		this.fileLoader = fileLoader;
		this.name = name;
		this.files = new ArrayList<ParsedFile>();
	}
	
	public ParserEnvironment getParserEnvironment() {
		return global;
	}
	
	public IZenErrorLogger getErrorLogger() {
		return global.getCompileEnvironment().getErrorLogger();
	}
	
	public void addScript(String filename, String contents) {
		try {
			files.add(new ParsedFile(this, filename, new ZenTokener(contents)));
		} catch (IOException ex) {
			global.getCompileEnvironment().getErrorLogger().error(
					null,
					"Could not load " + filename + ": " + ex.getMessage());
		}
	}
	
	public void addScript(File file) {
		try {
			Reader reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), UTF8);
			files.add(new ParsedFile(this, file.getName(), new ZenTokener(reader)));
		} catch (IOException ex) {
			global.getCompileEnvironment().getErrorLogger().error(
					null,
					"Could not load " + file.getName() + ": " + ex.getMessage());
		}
	}
	
	public InputStream loadFile(String name) {
		return fileLoader == null ? null : fileLoader.load(name);
	}
}
