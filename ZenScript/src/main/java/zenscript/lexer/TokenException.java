/*
 * This file is subject to the license.txt file in the main folder
 * of this project.
 */

package zenscript.lexer;

import zenscript.parser.ParsedFile;

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
