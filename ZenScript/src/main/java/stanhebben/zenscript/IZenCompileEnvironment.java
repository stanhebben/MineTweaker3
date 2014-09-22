/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript;

import zenscript.IZenErrorLogger;
import java.util.List;
import stanhebben.zenscript.compiler.IScopeGlobal;
import zenscript.lexer.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.runtime.IAny;

/**
 * The compile environment is supplied by the running instance to provide an
 * interface between the compiler and the environment. The compile environment
 * is only used during compilation, not during execution.
 * 
 * @author Stan Hebben
 */
public interface IZenCompileEnvironment {
	/**
	 * Gets the error logger used to report warnings and errors.
	 * 
	 * @return error logger
	 */
	public IZenErrorLogger getErrorLogger();
	
	/**
	 * Gets a global symbol. Should return null if the symbol doesn't exist.
	 * 
	 * @param name global symbol name
	 * @return symbol
	 */
	public IZenSymbol getGlobal(String name);
	
	/**
	 * Gets a dollar symbol. Should return null if the symbol doesn't exist.
	 * 
	 * @param name variable name
	 * @return symbol
	 */
	public IZenSymbol getDollar(String name);
	
	/**
	 * Evaluates the bracketed value. Should return null if it can't be evaluated.
	 * Return a symbol that evaluates to the null value if you intend to return
	 * null.
	 * 
	 * @param environment
	 * @param tokens
	 * @return 
	 */
	public IZenSymbol getBracketed(IScopeGlobal environment, List<Token> tokens);
	
	/**
	 * Evaluates a global value. Should return null if it can't be evaluated
	 * compiled. Return AnyNull.INSTANCE if you intend to return null.
	 * 
	 * @param name global variable name
	 * @return evaluated value
	 */
	public IAny evalGlobal(String name);
	
	/**
	 * Evaluates the dollar value. Should return null if it can't be evaluated
	 * compiled. Return AnyNull.INSTANCE if you intend to return null.
	 * 
	 * @param name dollar variable name
	 * @return evaluated value
	 */
	public IAny evalDollar(String name);
	
	/**
	 * Evaluates the bracketed value. Should return null if it can't be evaluated
	 * and AnyNull.INSTANCE if you intend to return null.
	 * 
	 * @param tokens
	 * @return 
	 */
	public IAny evalBracketed(List<Token> tokens);
}
