/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.util;

import org.openzen.zencode.parser.ParsedFile;

/**
 *
 * @author Stanneke
 */
public class CodePosition
{
	public static CodePosition SYSTEM = new CodePosition("system", 0, 0);
	public static CodePosition NATIVE = new CodePosition("native", 0, 0);
	
	private final ParsedFile file;
	private final String name;
	private final int line;
	private final int offset;

	public CodePosition(ParsedFile file, int line, int offset)
	{
		if (line <= 0)
			throw new IllegalArgumentException("Line must be positive");

		this.file = file;
		this.name = file.getFileName();
		this.line = line;
		this.offset = offset;
	}
	
	public CodePosition(String filename, int line, int offset)
	{
		this.file = null;
		this.name = filename;
		this.line = line;
		this.offset = offset;
	}

	public ParsedFile getFile()
	{
		return file;
	}

	public int getLine()
	{
		return line;
	}

	public int getLineOffset()
	{
		return offset;
	}

	@Override
	public String toString()
	{
		return name + ":" + Integer.toString(line);
	}
}
