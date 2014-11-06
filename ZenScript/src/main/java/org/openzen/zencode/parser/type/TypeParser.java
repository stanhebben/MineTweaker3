/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.type;

import java.io.IOException;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ParseException;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import static org.openzen.zencode.parser.type.ParsedTypeBasic.*;

/**
 *
 * @author Stan
 */
public class TypeParser {
	private TypeParser() {}
	
	public static ZenType parseDirect(String value, IScopeGlobal environment) {
		try {
			return parse(new ZenLexer(value), environment).compile(environment);
		} catch (IOException ex) {
			throw new RuntimeException("Could not parse type " + value, ex);
		}
	}
	
	public static IParsedType parse(ZenLexer tokener, ICodeErrorLogger errorLogger) {
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
