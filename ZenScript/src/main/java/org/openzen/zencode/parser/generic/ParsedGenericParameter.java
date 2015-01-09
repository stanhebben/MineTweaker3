/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.generic;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.parser.elements.IParsedGenericBound;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedGenericParameter
{
	public static ParsedGenericParameter parse(ZenLexer lexer)
	{
		CodePosition position = lexer.getPosition();
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

		return new ParsedGenericParameter(position, name, bounds);
	}

	private final CodePosition position;
	private final String name;
	private final List<IParsedGenericBound> bounds;

	public ParsedGenericParameter(CodePosition position, String name, List<IParsedGenericBound> bounds)
	{
		this.position = position;
		this.name = name;
		this.bounds = bounds;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public String getName()
	{
		return name;
	}

	public List<IParsedGenericBound> getBounds()
	{
		return bounds;
	}
}
