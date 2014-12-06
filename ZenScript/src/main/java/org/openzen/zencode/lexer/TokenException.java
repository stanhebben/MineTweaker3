/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.lexer;

import org.openzen.zencode.parser.ParsedFile;

/**
 * Thrown when an exception occurs while tokening a character stream.
 *
 * @author Stan Hebben
 */
public class TokenException extends RuntimeException {
    public TokenException(ParsedFile file, int line, int lineOffset, char value) {
        super("Invalid character at " + file + ":" + line + " - " + value);
		
		if (line < 0) throw new IllegalArgumentException("Line cannot be negative");
    }
}
