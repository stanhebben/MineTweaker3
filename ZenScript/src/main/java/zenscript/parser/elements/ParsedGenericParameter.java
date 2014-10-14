/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zenscript.parser.elements;

import java.util.ArrayList;
import java.util.List;
import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.T_IMPLEMENTS;
import static zenscript.lexer.ZenTokener.T_THIS;
import zenscript.parser.type.IParsedType;
import zenscript.parser.type.TypeParser;

/**
 *
 * @author Stan
 */
public class ParsedGenericParameter
{
	public static ParsedGenericParameter parse(ZenTokener tokener, IZenErrorLogger errors)
	{
		String name = tokener.requiredIdentifier();
		List<IParsedGenericBound> bounds = new ArrayList<IParsedGenericBound>();

		while (true) {
			if (tokener.optional(T_IMPLEMENTS) != null) {
				IParsedType type = TypeParser.parse(tokener, errors);
				bounds.add(new ParsedGenericBoundImplements(type));
			} else if (tokener.optional(T_THIS) != null) {
				ParsedFunctionSignature signature = ParsedFunctionSignature.parse(tokener, errors, null);
				bounds.add(new ParsedGenericBoundConstructor(signature));
			} else {
				break;
			}
		}

		return new ParsedGenericParameter(name, bounds);
	}

	private final String name;
	private final List<IParsedGenericBound> bounds;

	public ParsedGenericParameter(String name, List<IParsedGenericBound> bounds)
	{
		this.name = name;
		this.bounds = bounds;
	}
}
