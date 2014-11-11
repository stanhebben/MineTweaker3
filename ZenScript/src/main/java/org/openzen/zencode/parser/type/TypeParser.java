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
 * Utility class to parse types.
 *
 * @author Stan Hebben
 */
public class TypeParser
{
	private TypeParser()
	{
	}

	public static ZenType parseDirect(String value, IScopeGlobal scope)
	{
		try {
			return parse(new ZenLexer(value), scope).compile(scope);
		} catch (IOException ex) {
			throw new RuntimeException("Could not parse type " + value, ex);
		}
	}

	public static IParsedType parse(ZenLexer lexer, ICodeErrorLogger errors)
	{
		IParsedType result;

		Token firstToken = lexer.peek();
		switch (firstToken.getType()) {
			case T_ANY:
				lexer.next();
				result = ANY;
				break;

			case T_VOID:
				lexer.next();
				result = VOID;
				break;

			case T_BOOL:
				lexer.next();
				result = BOOL;
				break;

			case T_BYTE:
				lexer.next();
				result = BYTE;
				break;

			case T_SHORT:
				lexer.next();
				result = SHORT;
				break;

			case T_INT:
				lexer.next();
				result = INT;
				break;

			case T_LONG:
				lexer.next();
				result = LONG;
				break;

			case T_FLOAT:
				lexer.next();
				result = FLOAT;
				break;

			case T_DOUBLE:
				lexer.next();
				result = DOUBLE;
				break;

			case T_STRING:
				lexer.next();
				result = STRING;
				break;

			case TOKEN_ID:
				result = new ParsedTypeClass(errors, lexer);
				break;

			default:
				throw new ParseException(firstToken, "Unknown type: " + firstToken.getValue());
		}

		while (lexer.hasNext()) {
			Token token = lexer.peek();

			if (token.getType() == T_QUEST) {
				lexer.next();
				result = new ParsedTypeNullable(firstToken.getPosition(), result);

			} else if (token.getType() == T_SQBROPEN) {
				lexer.next();

				if (lexer.optional(T_SQBRCLOSE) == null) {
					IParsedType keyType = parse(lexer, errors);
					result = new ParsedTypeAssociative(keyType, result);

					lexer.required(T_SQBRCLOSE, "] expected");
				} else
					result = new ParsedTypeArray(result);
			} else
				break;
		}

		return result;
	}
}
