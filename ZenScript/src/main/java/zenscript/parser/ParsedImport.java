/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser;

import java.util.ArrayList;
import java.util.List;
import zenscript.IZenErrorLogger;
import zenscript.lexer.Token;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.TOKEN_ID;
import static zenscript.lexer.ZenTokener.T_AS;
import static zenscript.lexer.ZenTokener.T_DOT;
import static zenscript.lexer.ZenTokener.T_IMPORT;
import static zenscript.lexer.ZenTokener.T_SEMICOLON;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedImport {
	public static ParsedImport parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		
		/*for (Import imprt : imports) {
			List<String> name = imprt.getName();
			IPartialExpression type = null;
			
			StringBuilder nameSoFar = new StringBuilder();
			
			for (String part : name) {
				if (type == null) {
					nameSoFar.append(part);
					type = environment.getValue(part, imprt.getPosition());
					if (type == null) {
						environment.error(imprt.getPosition(), "could not find package " + type);
						break;
					}
				} else {
					nameSoFar.append('.').append(part);
					type = type.getMember(imprt.getPosition(), environment, part);
					if (type == null) {
						environment.error(imprt.getPosition(), "could not find type or package " + nameSoFar);
						break;
					}
				}
			}
			
			if (type != null) {
				IZenSymbol symbol = type.toSymbol();
				if (symbol == null) {
					environmentScript.error(imprt.getPosition(), "Not a valid type");
				} else {
					environmentScript.putValue(imprt.getRename(), type.toSymbol(), imprt.getPosition());
				}
			} else {
				environmentScript.putValue(imprt.getRename(), new SymbolType(ZenType.ANY), imprt.getPosition());
			}
		}*/
		
		ZenPosition position = tokener.required(T_IMPORT, "import expected").getPosition();
		
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
	
	private final ZenPosition position;
	private final List<String> importName;
	private final String rename;
	
	public ParsedImport(ZenPosition position, List<String> importName, String rename) {
		this.position = position;
		this.importName = importName;
		this.rename = rename;
	}
	
	public ZenPosition getPosition() {
		return position;
	}
	
	public List<String> getImportName() {
		return importName;
	}
	
	public String getRename() {
		return rename;
	}
}
