/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.iterator;

import org.objectweb.asm.Label;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stanneke
 */
public interface IJavaIterator {
	/**
	 * Compiles the header before the iteration. The list is on the top of the stack.
	 * 
	 * @param output
	 * @param locals
	 */
	public void compileStart(MethodOutput output, int[] locals);
	
	/**
	 * Compiles the start of an iteration. The stack is unmodified from the 
	 * previous iteration and from the iteration start.
	 * 
	 * @param output
	 * @param locals
	 * @param exit 
	 */
	public void compilePreIterate(MethodOutput output, int[] locals, Label exit);
	
	/**
	 * Compiles the end of an iteration. The stack is the same as it was after
	 * preIterate.
	 * 
	 * @param output
	 * @param locals
	 * @param exit
	 * @param repeat 
	 */
	public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat);
	
	/**
	 * Compiles the end of the whole iteration.
	 * 
	 * @param output
	 */
	public void compileEnd(MethodOutput output);
	
	public IGenericType<IJavaExpression> getType(int i);
}
