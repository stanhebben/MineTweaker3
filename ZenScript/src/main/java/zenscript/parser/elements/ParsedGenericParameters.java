/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zenscript.parser.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;

/**
 *
 * @author Stan
 */
public class ParsedGenericParameters
{
	public static List<ParsedGenericParameter> parse(ZenTokener tokener, IZenErrorLogger errors)
	{
		if (tokener.optional(ZenTokener.T_LT) == null)
			return null;

		if (tokener.optional(ZenTokener.T_GT) != null)
			return Collections.EMPTY_LIST;

		List<ParsedGenericParameter> parameters = new ArrayList<ParsedGenericParameter>();
		do {
			parameters.add(ParsedGenericParameter.parse(tokener, errors));

			if (tokener.optional(ZenTokener.T_LT) == null)
				break;

		} while (tokener.optional(ZenTokener.T_COMMA) != null);

		return parameters;
	}
}
