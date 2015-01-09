/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class StatementBlockScope<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		implements IMethodScope<E, T>
{
	private final IMethodScope<E, T> outer;
	private final Map<String, IZenSymbol<E, T>> local;
	private final Map<SymbolLocal<E, T>, Integer> locals;

	private final Statement<E, T> controlStatement;
	private final List<String> labels;

	public StatementBlockScope(IMethodScope<E, T> outer)
	{
		this.outer = outer;
		this.local = new HashMap<String, IZenSymbol<E, T>>();
		this.locals = new HashMap<SymbolLocal<E, T>, Integer>();
		this.controlStatement = null;
		this.labels = null;
	}

	public StatementBlockScope(IMethodScope<E, T> outer, Statement<E, T> controlStatement, String label)
	{
		this.outer = outer;
		this.local = new HashMap<String, IZenSymbol<E, T>>();
		this.locals = new HashMap<SymbolLocal<E, T>, Integer>();
		this.controlStatement = controlStatement;
		this.labels = label == null ? null : Collections.singletonList(label);
	}

	public StatementBlockScope(IMethodScope<E, T> outer, Statement<E, T> controlStatement, List<String> labels)
	{
		this.outer = outer;
		this.local = new HashMap<String, IZenSymbol<E, T>>();
		this.locals = new HashMap<SymbolLocal<E, T>, Integer>();
		this.controlStatement = controlStatement;
		this.labels = labels;
	}
	
	@Override
	public ISymbolicDefinition<E, T> getDefinition()
	{
		return outer.getDefinition();
	}

	@Override
	public AccessScope getAccessScope()
	{
		return outer.getAccessScope();
	}

	@Override
	public ITypeCompiler<E, T> getTypeCompiler()
	{
		return outer.getTypeCompiler();
	}

	@Override
	public IZenCompileEnvironment<E, T> getEnvironment()
	{
		return outer.getEnvironment();
	}
	
	@Override
	public IExpressionCompiler<E, T> getExpressionCompiler()
	{
		return outer.getExpressionCompiler();
	}

	@Override
	public IMethodScope<E, T> getConstantEnvironment()
	{
		return outer.getConstantEnvironment();
	}

	@Override
	public String makeClassName()
	{
		return outer.makeClassName();
	}

	@Override
	public boolean containsClass(String name)
	{
		return outer.containsClass(name);
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		outer.putClass(name, data);
	}
	
	@Override
	public Map<String, byte[]> getClasses()
	{
		return outer.getClasses();
	}

	@Override
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IMethodScope<E, T> environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else
			return outer.getValue(name, position, environment);
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
		return outer.getClassNames();
	}

	@Override
	public byte[] getClass(String name)
	{
		return outer.getClass(name);
	}

	@Override
	public Statement<E, T> getControlStatement(String label)
	{
		if (label == null && controlStatement != null)
			return controlStatement;
		else if (this.labels != null && this.labels.contains(label))
			return controlStatement;
		else
			return outer.getControlStatement(label);
	}

	@Override
	public T getReturnType()
	{
		return outer.getReturnType();
	}

	@Override
	public ICodeErrorLogger<E, T> getErrorLogger()
	{
		return outer.getErrorLogger();
	}

	@Override
	public MethodHeader<E, T> getMethodHeader()
	{
		return outer.getMethodHeader();
	}

	@Override
	public TypeCapture<E, T> getTypeCapture()
	{
		return outer.getTypeCapture();
	}
}
