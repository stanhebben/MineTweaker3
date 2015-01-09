/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser;

import org.openzen.zencode.parser.modifier.IParsedModifier;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.modifier.ModifierParser;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.parser.unit.IParsedDefinition;
import org.openzen.zencode.parser.unit.DefinitionParser;
import org.openzen.zencode.util.Strings;
import org.openzen.zencode.util.CodePosition;

/**
 * Contains a parsed file.
 *
 * A parsed file contains:
 * <ul>
 * <li>A set of imports</li>
 * <li>A set of parsed functions</li>
 * <li>A set of statuments</li>
 * </ul>
 *
 * This parsed file cannot be executed by itself, but it can be compiled into a
 * module, possibly together with other files.
 *
 * @author Stan Hebben
 */
public class ParsedFile
{
	private final ParsedModule module;
	private final String filename;

	private final ParsedPackage _package;
	private final List<IParsedDefinition> definitions;
	private final List<ParsedStatement> statements;

	/**
	 * Constructs and parses a given file.
	 *
	 * @param module module this file is part of
	 * @param filename parsed filename
	 * @param lexer input lexer
	 */
	public ParsedFile(ParsedModule module, String filename, ZenLexer lexer)
	{
		this.filename = filename;
		this.module = module;
		
		definitions = new ArrayList<IParsedDefinition>();
		statements = new ArrayList<ParsedStatement>();

		lexer.setFile(this);

		_package = parseOptionalPackage(lexer);
		parseScriptElements(lexer);
	}

	/**
	 * Gets the input filename for this file.
	 *
	 * @return input filename
	 */
	public String getFileName()
	{
		return filename;
	}
	
	public ParsedPackage getPackage()
	{
		return _package;
	}

	/**
	 * Gets this file's script statements.
	 *
	 * @return script statement list
	 */
	public List<ParsedStatement> getStatements()
	{
		return statements;
	}
	
	public List<IParsedDefinition> getDefinitions()
	{
		return definitions;
	}

	// #############################
	// ### Object implementation ###
	// #############################
	
	@Override
	public String toString()
	{
		return filename;
	}

	// #######################
	// ### Private methods ###
	// #######################
	
	private void tryLoadFileContents(CodePosition position, String filename)
	{
		try {
			InputStream file = module.loadFile(Strings.unescapeString(filename));
			if (file == null)
				module.getErrorLogger().errorCannotLoadInclude(position, filename);
			else
				parseScriptElements(ZenLexer.fromInputStream(module.getErrorLogger(), file));
		} catch (IOException ex) {
			module.getErrorLogger().errorCannotLoadInclude(position, filename);
		}
	}

	private void parseScriptElements(ZenLexer lexer)
	{
		List<ParsedAnnotation> annotations = ParsedAnnotation.parseAll(lexer);
		List<IParsedModifier> modifiers = ModifierParser.parseUnitModifiers(lexer);
		
		while (lexer.hasNext()) {
			Token next = lexer.peek();
			switch (next.getType()) {
				case T_INCLUDE:
					if (annotations != null && !annotations.isEmpty())
						module.getErrorLogger().errorAnnotationsForInclude(lexer.getPosition());
					
					if (modifiers != null && !modifiers.isEmpty())
						module.getErrorLogger().errorModifiersForInclude(lexer.getPosition());
					
					readInclude(lexer);
					break;
					
				case T_CLASS:
				case T_INTERFACE:
				case T_ENUM:
				case T_STRUCT:
				case T_EXPAND:
				case T_FUNCTION:
					definitions.add(DefinitionParser.parse(lexer, annotations, modifiers));
					break;

				default:
					if (modifiers != null && !modifiers.isEmpty())
						module.getErrorLogger().errorModifiersForStatement(lexer.getPosition());
					
					if (annotations != null && !annotations.isEmpty())
						module.getErrorLogger().errorAnnotationsForStatement(lexer.getPosition());
					
					statements.add(ParsedStatement.parse(lexer));
					break;
			}
		}
	}

	private void readInclude(ZenLexer lexer)
	{
		// consume "include" token
		lexer.next();

		Token includeFileName = lexer.required(T_STRINGVALUE, "string literal expected");
		lexer.requiredSemicolon();

		tryLoadFileContents(includeFileName.getPosition(), includeFileName.getValue());
	}
	
	private static ParsedPackage parseOptionalPackage(ZenLexer lexer)
	{
		if (lexer.optional(T_PACKAGE) == null)
			return null;
		
		List<String> nameParts = lexer.parseIdentifierDotSequence();
		lexer.requiredSemicolon();

		return new ParsedPackage(nameParts);
	}
}
