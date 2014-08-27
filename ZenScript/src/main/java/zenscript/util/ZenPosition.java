/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.util;

import zenscript.parser.ParsedFile;

/**
 *
 * @author Stanneke
 */
public class ZenPosition {
	private final ParsedFile file;
	private final int line;
	private final int offset;
	
	public ZenPosition(ParsedFile file, int line, int offset) {
		if (file != null && line <= 0) throw new IllegalArgumentException("Line must be positive");
		
		this.file = file;
		this.line = line;
		this.offset = offset;
	}
	
	public ParsedFile getFile() {
		return file;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getLineOffset() {
		return offset;
	}
	
	@Override
	public String toString() {
		return (file == null ? "?" : file.getFileName()) + ":" + Integer.toString(line);
	}
}
