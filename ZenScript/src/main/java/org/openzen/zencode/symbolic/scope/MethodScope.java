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
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 * @param <T>
 */
public class MethodScope<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IMethodScope<E, T>
{
	private final IDefinitionScope<E, T> scope;
	private final Map<String, IZenSymbol<E, T>> local;
	private final MethodHeader<E, T> methodHeader;

	public MethodScope(IDefinitionScope<E, T> environment, MethodHeader<E, T> methodHeader)
	{
		this.scope = environment;
		this.local = new HashMap<String, IZenSymbol<E, T>>();
		this.methodHeader = methodHeader;
	}
	
	@Override
	public ISymbolicDefinition<E, T> getDefinition()
	{
		return scope.getDefinition();
	}

	@Override
	public AccessScope getAccessScope()
	{
		return scope.getAccessScope();
	}

	@Override
	public ITypeCompiler<E, T> getTypeCompiler()
	{
		return scope.getTypeCompiler();
	}
	
	@Override
	public IMethodScope<E, T> getConstantEnvironment()
	{
		return scope.getConstantEnvironment();
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
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IMethodScope<E, T> environment)
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
			getErrorLogger().errorSymbolNameAlreadyExists(position, name);
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
		return methodHeader.getReturnType();
	}

	@Override
	public ICodeErrorLogger<E, T> getErrorLogger()
	{
		return scope.getErrorLogger();
	}

	@Override
	public MethodHeader<E, T> getMethodHeader()
	{
		return methodHeader;
	}
}
