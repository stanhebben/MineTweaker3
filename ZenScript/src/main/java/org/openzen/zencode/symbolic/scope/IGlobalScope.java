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
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan Hebben
 * @param <E>
 * @param <T>
 */
public interface IGlobalScope<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public IZenCompileEnvironment<E, T> getEnvironment();

	public ITypeCompiler<E, T> getTypeCompiler();

	public IExpressionCompiler<E, T> getExpressionCompiler();

	public IMethodScope<E, T> getConstantEnvironment();
	
	public String makeClassName();

	public boolean containsClass(String name);

	public Set<String> getClassNames();

	public byte[] getClass(String name);

	public void putClass(String name, byte[] data);

	public Map<String, byte[]> getClasses();

	public IPartialExpression<E, T> getValue(String name, CodePosition position, IMethodScope<E, T> environment);

	public void putValue(String name, IZenSymbol<E, T> value, CodePosition position);
	
	public ICodeErrorLogger<E, T> getErrorLogger();
	
	//public Package getRootPackage();
}
