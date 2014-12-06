/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedVal implements IParsedMember
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final String name;
	private final IParsedType asType;
	private final ParsedExpression initializer;
	private final List<ParsedAccessor> accessors;
	
	public ParsedVal(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			String name,
			IParsedType asType,
			ParsedExpression initializer,
			List<ParsedAccessor> accessors)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.name = name;
		this.asType = asType;
		this.initializer = initializer;
		this.accessors = accessors;
	}
}
