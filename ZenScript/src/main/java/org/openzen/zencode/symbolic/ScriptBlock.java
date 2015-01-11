/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

import java.util.List;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.symbolic.expression.IPartialExpression;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ScriptBlock<E extends IPartialExpression<E>>
{
	private final String filename;
	private final List<ParsedStatement> sourceStatements;
	
	public ScriptBlock(String filename, List<ParsedStatement> sourceStatements)
	{
		this.filename = filename;
		this.sourceStatements = sourceStatements;
	}

	public String getFilename()
	{
		return filename;
	}

	public List<ParsedStatement> getSourceStatements()
	{
		return sourceStatements;
	}
}
