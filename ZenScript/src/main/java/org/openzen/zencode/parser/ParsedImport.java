/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.TOKEN_ID;
import static org.openzen.zencode.lexer.ZenLexer.T_AS;
import static org.openzen.zencode.lexer.ZenLexer.T_DOT;
import static org.openzen.zencode.lexer.ZenLexer.T_IMPORT;
import static org.openzen.zencode.lexer.ZenLexer.T_SEMICOLON;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedImport {
	public static ParsedImport parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
		CodePosition position = tokener.required(T_IMPORT, "import expected").getPosition();
		
		List<String> importName = new ArrayList<String>();
		Token tName = tokener.required(TOKEN_ID, "identifier expected");
		importName.add(tName.getValue());

		while (tokener.optional(T_DOT) != null) {
			Token tNamePart = tokener.required(TOKEN_ID, "identifier expected");
			importName.add(tNamePart.getValue());
		}

		String rename = null;
		if (tokener.optional(T_AS) != null) {
			Token tRename = tokener.required(TOKEN_ID, "identifier expected");
			rename = tRename.getValue();
		}

		tokener.required(T_SEMICOLON, "; expected");

		return new ParsedImport(position, importName, rename);
	}
	
	private final CodePosition position;
	private final List<String> importName;
	private final String rename;
	
	public ParsedImport(CodePosition position, List<String> importName, String rename) {
		this.position = position;
		this.importName = importName;
		this.rename = rename;
	}
	
	public CodePosition getPosition() {
		return position;
	}
	
	public List<String> getImportName() {
		return importName;
	}
	
	public String getRename() {
		return rename;
	}
}
