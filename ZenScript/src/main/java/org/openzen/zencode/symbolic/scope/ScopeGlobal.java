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
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.ClassNameGenerator;

/**
 *
 * @author Stanneke
 * @param <E>
 * @param <T>
 */
public class ScopeGlobal<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements IScopeGlobal<E, T>
{
	private final IZenCompileEnvironment<E, T> environment;
	private final ICodeErrorLogger errors;
	private final Map<String, byte[]> classes;
	private final Map<String, IZenSymbol<E, T>> local;
	private final ClassNameGenerator nameGen;
	private final ScopeConstant<E, T> constantScope;

	public ScopeGlobal(
			IZenCompileEnvironment<E, T> environment,
			ClassNameGenerator nameGen)
	{
		this.environment = environment;
		this.errors = environment.getErrorLogger();
		this.classes = new HashMap<String, byte[]>();
		this.nameGen = nameGen;
		this.local = new HashMap<String, IZenSymbol<E, T>>();
		constantScope = new ScopeConstant<E, T>(this);
	}

	@Override
	public IZenCompileEnvironment<E, T> getEnvironment()
	{
		return environment;
	}

	@Override
	public IExpressionCompiler<E, T> getExpressionCompiler()
	{
		return environment.getExpressionCompiler();
	}
	
	@Override
	public IScopeMethod<E, T> getConstantEnvironment()
	{
		return constantScope;
	}

	@Override
	public ITypeCompiler<E, T> getTypes()
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
	public boolean hasErrors()
	{
		return errors.hasErrors();
	}

	@Override
	public void error(CodePosition position, String message)
	{
		errors.error(position, message);
	}

	@Override
	public void warning(CodePosition position, String message)
	{
		errors.warning(position, message);
	}

	@Override
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IScopeMethod<E, T> scope)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, scope);
		else
			return this.environment.getGlobal(position, scope, name);
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
		return classes.keySet();
	}

	@Override
	public byte[] getClass(String name)
	{
		return classes.get(name);
	}
}
