/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.elements;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;

/**
 *
 * @author Stan
 */
public class ParsedGenericParameter
{
	public static ParsedGenericParameter parse(ZenLexer lexer)
	{
		String name = lexer.requiredIdentifier();
		List<IParsedGenericBound> bounds = new ArrayList<IParsedGenericBound>();

		while (true) {
			if (lexer.optional(T_IMPLEMENTS) != null) {
				IParsedType type = TypeParser.parse(lexer);
				bounds.add(new ParsedGenericBoundImplements(type));
			} else if (lexer.optional(T_EXTENDS) != null) {
				IParsedType type = TypeParser.parse(lexer);
				bounds.add(new ParsedGenericBoundExtends(type));
			} else if (lexer.optional(T_THIS) != null) {
				ParsedFunctionSignature signature = ParsedFunctionSignature.parse(lexer);
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
