/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.scope;

import java.util.Map;
import java.util.Set;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan Hebben
 */
public interface IScopeGlobal extends ICodeErrorLogger {
	public TypeRegistry getTypes();
	
	public IZenCompileEnvironment getEnvironment();
	
	public String makeClassName();
	
	public boolean containsClass(String name);
	
	public Set<String> getClassNames();
	
	public byte[] getClass(String name);
	
	public void putClass(String name, byte[] data);
	
	public Map<String, byte[]> getClasses();
	
	public IPartialExpression getValue(String name, CodePosition position, IScopeMethod environment);
	
	public void putValue(String name, IZenSymbol value, CodePosition position);
}
