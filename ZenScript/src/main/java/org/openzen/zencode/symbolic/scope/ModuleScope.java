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
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ModuleScope<E extends IPartialExpression<E>>
	implements IModuleScope<E>
{
	private final IGlobalScope<E> parent;
	private final Map<String, IZenSymbol<E>> imports;
	private final AccessScope accessScope;

	public ModuleScope(IGlobalScope<E> parent)
	{
		this.parent = parent;
		imports = new HashMap<String, IZenSymbol<E>>();
		accessScope = AccessScope.createModuleScope();
	}

	@Override
	public AccessScope getAccessScope()
	{
		return accessScope;
	}

	@Override
	public IZenCompileEnvironment<E> getEnvironment()
	{
		return parent.getEnvironment();
	}
	
	@Override
	public IExpressionCompiler<E> getExpressionCompiler()
	{
		return parent.getExpressionCompiler();
	}
	
	@Override
	public IMethodScope<E> getConstantEnvironment()
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
	public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> environment)
	{
		if (imports.containsKey(name)) {
			IZenSymbol<E> imprt = imports.get(name);
			if (imprt == null)
				throw new RuntimeException("How could this happen?");
			return imprt.instance(position, environment);
		} else
			return parent.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E> value, CodePosition position)
	{
		if (value == null)
			throw new IllegalArgumentException("value cannot be null");

		if (imports.containsKey(name))
			getErrorLogger().errorSymbolNameAlreadyExists(position, name);
		else
			imports.put(name, value);
	}

	@Override
	public ITypeCompiler<E> getTypeCompiler()
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
	public ICodeErrorLogger<E> getErrorLogger()
	{
		return parent.getErrorLogger();
	}

	@Override
	public TypeCapture<E> getTypeCapture()
	{
		return TypeCapture.empty();
	}
}
