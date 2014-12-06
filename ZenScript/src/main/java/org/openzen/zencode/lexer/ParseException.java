/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.lexer;

import org.openzen.zencode.parser.ParsedFile;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParseException extends RuntimeException {
	private final Token token;
	private final String message;
	
    public ParseException(Token token, String error) {
        super("Error parsing " + token.getPosition() + " - " + error + " (last token: " + token.getValue() + ")");
        
        this.token = token;
        this.message = error;
    }
	
	public ParseException(ParsedFile file, int line, int lineOffset, String error) {
		token = new Token(null, 0, new CodePosition(file, line, lineOffset));
		message = error;
	}
    
    public CodePosition getPosition() {
		return token.getPosition();
	}
    
    public String getExplanation() {
    	return message;
    }
}
