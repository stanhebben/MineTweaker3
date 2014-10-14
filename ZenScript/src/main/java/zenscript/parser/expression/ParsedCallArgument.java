/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.parser.expression;

import zenscript.IZenErrorLogger;
import zenscript.lexer.ZenTokener;
import static zenscript.lexer.ZenTokener.T_COLON;

/**
 *
 * @author Stan
 */
public class ParsedCallArgument
{
	public static ParsedCallArgument parse(ZenTokener tokener, IZenErrorLogger errors)
	{
		ParsedExpression expression = ParsedExpression.parse(tokener, errors);

		if (tokener.optional(T_COLON) == null)
			return new ParsedCallArgument(null, expression);

		String key = expression.asIdentifier();
		if (key == null)
			errors.error(expression.getPosition(), "Invalid key");

		return new ParsedCallArgument(key, ParsedExpression.parse(tokener, errors));
	}

	private final String key;
	private final ParsedExpression value;

	public ParsedCallArgument(String key, ParsedExpression value)
	{
		this.key = key;
		this.value = value;
	}

	public String getKey()
	{
		return key;
	}

	public ParsedExpression getValue()
	{
		return value;
	}
}
