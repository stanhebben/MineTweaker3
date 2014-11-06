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
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.ClassNameGenerator;

/**
 *
 * @author Stanneke
 */
public class ScopeGlobal implements IScopeGlobal
{
	private final IZenCompileEnvironment environment;
	private final ICodeErrorLogger errors;
	private final Map<String, byte[]> classes;
	private final Map<String, IZenSymbol> local;
	private final ClassNameGenerator nameGen;
	private final TypeRegistry types;

	public ScopeGlobal(
			IZenCompileEnvironment environment,
			Map<String, byte[]> classes,
			ClassNameGenerator nameGen)
	{
		this.environment = environment;
		this.errors = environment.getErrorLogger();
		this.classes = classes;
		this.nameGen = nameGen;
		this.types = new TypeRegistry(this);
		this.local = new HashMap<String, IZenSymbol>();
	}

	public IZenCompileEnvironment getCompileEnvironment()
	{
		return environment;
	}

	@Override
	public TypeRegistry getTypes()
	{
		return types;
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
	public String makeClassName()
	{
		return nameGen.generate();
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
	public IZenCompileEnvironment getEnvironment()
	{
		return environment;
	}

	@Override
	public IPartialExpression getValue(String name, CodePosition position, IScopeMethod environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else {
			IZenSymbol symbol = this.environment.getGlobal(name);
			return symbol == null ? null : symbol.instance(position, environment);
		}
	}

	@Override
	public void putValue(String name, IZenSymbol value, CodePosition position)
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
