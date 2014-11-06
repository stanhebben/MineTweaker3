/*
 * This file is subject to the license.txt file in the main folder
 * of this project.
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
