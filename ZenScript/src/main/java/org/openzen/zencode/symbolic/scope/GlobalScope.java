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
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.ClassNameGenerator;

/**
 *
 * @author Stanneke
 * @param <E>
 */
public class GlobalScope<E extends IPartialExpression<E>> implements IGlobalScope<E>
{
	private final IZenCompileEnvironment<E> environment;
	private final ICodeErrorLogger<E> errors;
	private final Map<String, byte[]> classes;
	private final Map<String, IZenSymbol<E>> local;
	private final ClassNameGenerator nameGen;
	private final ConstantScope<E> constantScope;

	public GlobalScope(
			IZenCompileEnvironment<E> environment,
			ClassNameGenerator nameGen)
	{
		this.environment = environment;
		this.errors = environment.getErrorLogger();
		this.classes = new HashMap<String, byte[]>();
		this.nameGen = nameGen;
		this.local = new HashMap<String, IZenSymbol<E>>();
		constantScope = new ConstantScope<E>(this);
	}

	@Override
	public IZenCompileEnvironment<E> getEnvironment()
	{
		return environment;
	}

	@Override
	public IExpressionCompiler<E> getExpressionCompiler()
	{
		return environment.getExpressionCompiler();
	}
	
	@Override
	public IMethodScope<E> getConstantEnvironment()
	{
		return constantScope;
	}

	@Override
	public ITypeCompiler<E> getTypeCompiler()
	{
		return environment.getTypeCompiler();
	}

	@Override
	public boolean containsClass(String name)
	{
		return classes.containsKey(name);
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		classes.put(name, data);
	}

	@Override
	public Map<String, byte[]> getClasses()
	{
		return classes;
	}

	@Override
	public String makeClassName()
	{
		return nameGen.generate();
	}
	
	@Override
	public ICodeErrorLogger<E> getErrorLogger()
	{
		return errors;
	}

	@Override
	public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> scope)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, scope);
		else
			return this.environment.getGlobal(position, scope, name);
	}

	@Override
	public void putValue(String name, IZenSymbol<E> value, CodePosition position)
	{
		if (local.containsKey(name))
			getErrorLogger().errorSymbolNameAlreadyExists(position, name);
		else
			local.put(name, value);
	}

	@Override
	public Set<String> getClassNames()
	{
		return classes.keySet();
	}

	@Override
	public byte[] getClass(String name)
	{
		return classes.get(name);
	}
}
