/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.lexer.ZenLexer;

/**
 *
 * @author Stan
 */
public class ParsedGenericParameters
{
	public static List<ParsedGenericParameter> parse(ZenLexer lexer)
	{
		if (lexer.optional(ZenLexer.T_LT) == null)
			return null;

		if (lexer.optional(ZenLexer.T_GT) != null)
			return Collections.emptyList();

		List<ParsedGenericParameter> parameters = new ArrayList<ParsedGenericParameter>();
		do {
			parameters.add(ParsedGenericParameter.parse(lexer));

			if (lexer.optional(ZenLexer.T_LT) == null)
				break;

		} while (lexer.optional(ZenLexer.T_COMMA) != null);

		return parameters;
	}
}
