/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.compiler;

import java.util.Set;
import zenscript.IZenErrorLogger;
import stanhebben.zenscript.TypeExpansion;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.symbolic.TypeRegistry;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public interface IScopeGlobal extends IZenErrorLogger {
	public TypeRegistry getTypes();
	
	public IZenCompileEnvironment getEnvironment();
	
	public TypeExpansion getExpansion(String name);
	
	public String makeClassName();
	
	public boolean containsClass(String name);
	
	public Set<String> getClassNames();
	
	public byte[] getClass(String name);
	
	public void putClass(String name, byte[] data);
	
	public IPartialExpression getValue(String name, ZenPosition position, IScopeMethod environment);
	
	public void putValue(String name, IZenSymbol value, ZenPosition position);
}
