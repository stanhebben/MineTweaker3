/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.lexer;

import org.openzen.zencode.util.CodePosition;

/**
 * Represents a token in a token stream.
 *
 * @author Stan Hebben
 */
public class Token {
	private final CodePosition position;
    private final String value;
    private final int type;

    /**
     * Constructs a new token.
     *
     * @param value token string value
     * @param type token type
	 * @param position token position
     */
    public Token(String value, int type, CodePosition position) {
        this.value = value;
        this.type = type;
		this.position = position;
    }
    
    public CodePosition getPosition() {
    	return position;
    }

    /**
     * Returns the string value of this token.
     *
     * @return token value
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the token type of this token.
     *
     * @return token type
     */
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return position + " (" + type + ") " + value;
    }
}
