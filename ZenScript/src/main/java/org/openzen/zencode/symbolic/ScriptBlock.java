/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

import java.util.List;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ScriptBlock<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	private final String filename;
	private final List<ParsedStatement> sourceStatements;
	
	public ScriptBlock(String filename, List<ParsedStatement> sourceStatements)
	{
		this.filename = filename;
		this.sourceStatements = sourceStatements;
	}
}
