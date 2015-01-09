/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ModuleScope<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	implements IModuleScope<E, T>
{
	private final IGlobalScope<E, T> parent;
	private final Map<String, IZenSymbol<E, T>> imports;
	private final AccessScope accessScope;

	public ModuleScope(IGlobalScope<E, T> parent)
	{
		this.parent = parent;
		imports = new HashMap<String, IZenSymbol<E, T>>();
		accessScope = AccessScope.createModuleScope();
	}

	@Override
	public AccessScope getAccessScope()
	{
		return accessScope;
	}

	@Override
	public IZenCompileEnvironment<E, T> getEnvironment()
	{
		return parent.getEnvironment();
	}
	
	@Override
	public IExpressionCompiler<E, T> getExpressionCompiler()
	{
		return parent.getExpressionCompiler();
	}
	
	@Override
	public IMethodScope<E, T> getConstantEnvironment()
	{
		return parent.getConstantEnvironment();
	}

	@Override
	public String makeClassName()
	{
		return parent.makeClassName();
	}

	@Override
	public boolean containsClass(String name)
	{
		return parent.containsClass(name);
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		parent.putClass(name, data);
	}
	
	@Override
	public Map<String, byte[]> getClasses()
	{
		return parent.getClasses();
	}

	@Override
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IMethodScope<E, T> environment)
	{
		if (imports.containsKey(name)) {
			IZenSymbol<E, T> imprt = imports.get(name);
			if (imprt == null)
				throw new RuntimeException("How could this happen?");
			return imprt.instance(position, environment);
		} else
			return parent.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E, T> value, CodePosition position)
	{
		if (value == null)
			throw new IllegalArgumentException("value cannot be null");

		if (imports.containsKey(name))
			getErrorLogger().errorSymbolNameAlreadyExists(position, name);
		else
			imports.put(name, value);
	}

	@Override
	public ITypeCompiler<E, T> getTypeCompiler()
	{
		return parent.getTypeCompiler();
	}

	@Override
	public Set<String> getClassNames()
	{
		return parent.getClassNames();
	}

	@Override
	public byte[] getClass(String name)
	{
		return parent.getClass(name);
	}

	@Override
	public ICodeErrorLogger<E, T> getErrorLogger()
	{
		return parent.getErrorLogger();
	}

	@Override
	public TypeCapture<E, T> getTypeCapture()
	{
		return TypeCapture.empty();
	}
}
