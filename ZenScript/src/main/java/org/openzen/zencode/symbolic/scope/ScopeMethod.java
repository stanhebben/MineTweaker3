/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 * @param <T>
 */
public class ScopeMethod<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IScopeMethod<E, T>
{
	private final IScopeClass<E, T> scope;
	private final Map<String, IZenSymbol<E, T>> local;
	private final T returnType;

	public ScopeMethod(IScopeClass<E, T> environment, T returnType)
	{
		this.scope = environment;
		this.local = new HashMap<String, IZenSymbol<E, T>>();
		this.returnType = returnType;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return scope.getAccessScope();
	}

	@Override
	public ITypeCompiler<E, T> getTypes()
	{
		return scope.getTypes();
	}
	
	@Override
	public IScopeMethod<E, T> getConstantEnvironment()
	{
		return scope.getConstantEnvironment();
	}
	
	@Override
	public boolean hasErrors()
	{
		return scope.hasErrors();
	}

	@Override
	public void error(CodePosition position, String message)
	{
		scope.error(position, message);
	}

	@Override
	public void warning(CodePosition position, String message)
	{
		scope.warning(position, message);
	}

	@Override
	public IZenCompileEnvironment<E, T> getEnvironment()
	{
		return scope.getEnvironment();
	}
	
	@Override
	public IExpressionCompiler<E, T> getExpressionCompiler()
	{
		return scope.getExpressionCompiler();
	}

	@Override
	public String makeClassName()
	{
		return scope.makeClassName();
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		scope.putClass(name, data);
	}

	@Override
	public boolean containsClass(String name)
	{
		return scope.containsClass(name);
	}

	@Override
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IScopeMethod<E, T> environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else
			return scope.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E, T> value, CodePosition position)
	{
		if (local.containsKey(name))
			error(position, "Value already defined in this scope: " + name);
		else
			local.put(name, value);
	}

	@Override
	public Set<String> getClassNames()
	{
		return scope.getClassNames();
	}

	@Override
	public byte[] getClass(String name)
	{
		return scope.getClass(name);
	}
	
	@Override
	public Map<String, byte[]> getClasses()
	{
		return scope.getClasses();
	}

	@Override
	public Statement<E, T> getControlStatement(String label)
	{
		return null;
	}

	@Override
	public T getReturnType()
	{
		return returnType;
	}
}
