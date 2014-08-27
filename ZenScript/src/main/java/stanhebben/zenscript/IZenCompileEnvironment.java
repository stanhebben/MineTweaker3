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
import zenscript.symbolic.TypeRegistry;

/**
 *
 * @author Stanneke
 */
public interface IZenCompileEnvironment {
	public IZenErrorLogger getErrorLogger();
	
	public IZenSymbol getGlobal(String name);
	
	public IZenSymbol getDollar(String name);
	
	public IZenSymbol getBracketed(IScopeGlobal environment, List<Token> tokens);
	
	public TypeRegistry getTypes();
	
	public TypeExpansion getExpansion(String type);
}
