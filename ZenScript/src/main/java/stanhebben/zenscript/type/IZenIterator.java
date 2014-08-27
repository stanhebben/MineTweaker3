/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import org.objectweb.asm.Label;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stanneke
 */
public interface IZenIterator {
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
	
	public ZenType getType(int i);
}
