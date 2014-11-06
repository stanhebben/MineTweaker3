package org.openzen.zencode.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.elements.ParsedFunction;
import org.openzen.zencode.parser.statement.ParsedStatement;
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
	private final List<ParsedImport> imports;
	private final Map<String, ParsedFunction> functions;
	private final List<ParsedStatement> statements;

	/**
	 * Constructs and parses a given file.
	 *
	 * @param module module this file is part of
	 * @param filename parsed filename
	 * @param tokener input tokener
	 */
	public ParsedFile(ParsedModule module, String filename, ZenLexer tokener)
	{
		this.filename = filename;
		this.module = module;

		imports = new ArrayList<ParsedImport>();
		functions = new HashMap<String, ParsedFunction>();
		statements = new ArrayList<ParsedStatement>();

		tokener.setFile(this);

		_package = parsePackage(tokener);
		loadFileContents(tokener);
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

	/**
	 * Gets the imports list.
	 *
	 * @return imports list
	 */
	public List<ParsedImport> getImports()
	{
		return imports;
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

	/**
	 * Gets the functions defined inside this file.
	 *
	 * @return script functions
	 */
	public Map<String, ParsedFunction> getFunctions()
	{
		return functions;
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
			if (file == null) {
				module.getErrorLogger().error(
						position,
						"Could not load file " + filename
				);
			} else {
					loadFileContents(ZenLexer.fromInputStream(file));
			}
		} catch (IOException ex) {
			module.getErrorLogger().error(position, "Could not load file " + filename);
		}
	}

	private void loadFileContents(ZenLexer tokener)
	{
		while (tokener.hasNext()) {
			Token next = tokener.peek();
			switch (next.getType()) {
				case T_IMPORT:
					imports.add(ParsedImport.parse(tokener, module.getErrorLogger()));
					break;

				case T_INCLUDE:
					readInclude(tokener);
					break;

				case T_CLASS:
					parseClass(tokener);
					break;

				case T_INTERFACE:
					parseInterface(tokener);
					break;

				case T_ENUM:
					parseEnum(tokener);
					break;

				case T_STRUCT:
					parseStruct(tokener);
					break;

				case T_EXPAND:
					parseExpansion(tokener);
					break;

				case T_FUNCTION:
					parseFunction(tokener, module.getErrorLogger());
					break;

				default:
					statements.add(ParsedStatement.parse(tokener, module.getErrorLogger()));
					break;
			}
		}
	}

	private void readInclude(ZenLexer tokener)
	{
		// process "include" token
		tokener.next();

		Token includeFileName = tokener.required(T_STRINGVALUE, "string literal expected");
		tokener.required(T_SEMICOLON, "; expected");

		tryLoadFileContents(includeFileName.getPosition(), includeFileName.getValue());
	}

	private void parseClass(ZenLexer tokener)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void parseInterface(ZenLexer tokener)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void parseEnum(ZenLexer tokener)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void parseStruct(ZenLexer tokener)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void parseExpansion(ZenLexer tokener)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void parseFunction(ZenLexer tokener, ICodeErrorLogger errorLogger)
	{
		ParsedFunction function = ParsedFunction.parse(tokener, errorLogger);

		if (functions.containsKey(function.getName())) {
			module.getErrorLogger().error(function.getPosition(), "function " + function.getName() + " already exists");
		} else {
			functions.put(function.getName(), function);
		}
	}

	private static ParsedPackage parsePackage(ZenLexer tokener)
	{
		if (tokener.optional(T_PACKAGE) == null) {
			return null;
		}

		List<String> name = tokener.parseIdentifierDotSequence();
		tokener.required(T_SEMICOLON, "; expected");

		return new ParsedPackage(Strings.join(name, "."));
	}
}
