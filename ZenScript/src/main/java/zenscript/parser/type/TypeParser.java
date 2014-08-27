/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.type;

import java.io.IOException;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ParseException;
import zenscript.lexer.Token;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.*;
import static zenscript.parser.type.ParsedTypeBasic.*;

/**
 *
 * @author Stan
 */
public class TypeParser {
	private TypeParser() {}
	
	public static ZenType parseDirect(String value, IScopeGlobal environment) {
		try {
			return parse(new ZenTokener(value), environment).compile(environment);
		} catch (IOException ex) {
			throw new RuntimeException("Could not parse type " + value, ex);
		}
	}
	
	public static IParsedType parse(ZenTokener tokener, IZenErrorLogger errorLogger) {
		IParsedType result;
		
		Token firstToken = tokener.next();
		switch (firstToken.getType()) {
			case T_ANY:
				result = ANY;
				break;
				
			case T_VOID:
				result = VOID;
				break;
				
			case T_BOOL:
				result = BOOL;
				break;
				
			case T_BYTE:
				result = BYTE;
				break;
				
			case T_SHORT:
				result = SHORT;
				break;
				
			case T_INT:
				result = INT;
				break;
				
			case T_LONG:
				result = LONG;
				break;
				
			case T_FLOAT:
				result = FLOAT;
				break;
				
			case T_DOUBLE:
				result = DOUBLE;
				break;
				
			case T_STRING:
				result = STRING;
				break;
				
			case TOKEN_ID:
				result = new ParsedTypeClass(errorLogger, tokener);
				break;
				
			default:
				throw new ParseException(firstToken, "Unknown type: " + firstToken.getValue());
		}
		
		while (tokener.hasNext()) {
			Token token = tokener.peek();
			
			if (token.getType() == T_QUEST) {
				tokener.next();
				result = new ParsedTypeNullable(firstToken.getPosition(), result);
				
			} else if (token.getType() == T_SQBRCLOSE) {
				tokener.next();
				
				if (tokener.optional(T_SQBRCLOSE) == null) {
					IParsedType keyType = parse(tokener, errorLogger);
					result = new ParsedTypeAssociative(result, keyType);
					
					tokener.required(T_SQBRCLOSE, "] expected");
				} else {
					result = new ParsedTypeArray(result);
				}
			} else {
				break;
			}
		}
		
		return result;
	}
}
