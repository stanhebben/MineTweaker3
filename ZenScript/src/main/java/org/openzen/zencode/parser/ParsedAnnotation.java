/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import static org.openzen.zencode.lexer.ZenLexer.*;
import org.openzen.zencode.parser.expression.ParsedCallArguments;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.parser.type.TypeParser;

/**
 *
 * @author Stan
 */
public class ParsedAnnotation
{
	public static List<ParsedAnnotation> parseAll(ZenLexer lexer)
	{
		if (lexer.peek().getType() != T_SQBROPEN)
			return Collections.emptyList();
		
		List<ParsedAnnotation> annotations = new ArrayList<ParsedAnnotation>();
		
		while (lexer.peek().getType() == T_SQBROPEN) {
			annotations.add(parse(lexer));
		}
		
		return annotations;
	}
	
	public static ParsedAnnotation parse(ZenLexer lexer)
	{
		lexer.required(T_SQBROPEN, "[ expected");
		
		IParsedType annotationType = TypeParser.parse(lexer);
		ParsedCallArguments arguments = null;
		
		if (lexer.peek().getType() == T_BROPEN)
			arguments = ParsedCallArguments.parse(lexer);
		
		lexer.required(T_SQBRCLOSE, "] expected");
		return new ParsedAnnotation(annotationType, arguments);
	}
	
	private final IParsedType annotationType;
	private final ParsedCallArguments arguments;
	
	private ParsedAnnotation(IParsedType annotationType, ParsedCallArguments arguments)
	{
		this.annotationType = annotationType;
		this.arguments = arguments;
	}
}
