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
import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 */
public class MethodScope<E extends IPartialExpression<E>> implements IMethodScope<E>
{
	private final IDefinitionScope<E> scope;
	private final Map<String, IZenSymbol<E>> local;
	private final MethodHeader<E> methodHeader;
	private final TypeCapture<E> typeCapture;
	
	public MethodScope(IDefinitionScope<E> environment, MethodHeader<E> methodHeader)
	{
		this.scope = environment;
		this.local = new HashMap<String, IZenSymbol<E>>();
		this.methodHeader = methodHeader;
		
		typeCapture = new TypeCapture<E>(environment.getTypeCapture());
		for (GenericParameter<E> parameter : methodHeader.getGenericParameters()) {
			typeCapture.put(parameter, environment.getTypeCompiler().getGeneric(parameter));
		}
	}
	
	@Override
	public ISymbolicDefinition<E> getDefinition()
	{
		return scope.getDefinition();
	}

	@Override
	public AccessScope getAccessScope()
	{
		return scope.getAccessScope();
	}

	@Override
	public ITypeCompiler<E> getTypeCompiler()
	{
		return scope.getTypeCompiler();
	}
	
	@Override
	public IMethodScope<E> getConstantEnvironment()
	{
		return scope.getConstantEnvironment();
	}

	@Override
	public IZenCompileEnvironment<E> getEnvironment()
	{
		return scope.getEnvironment();
	}
	
	@Override
	public IExpressionCompiler<E> getExpressionCompiler()
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
	public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else
			return scope.getValue(name, position, environment);
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
	public Statement<E> getControlStatement(String label)
	{
		return null;
	}

	@Override
	public TypeInstance<E> getReturnType()
	{
		return methodHeader.getReturnType();
	}

	@Override
	public ICodeErrorLogger<E> getErrorLogger()
	{
		return scope.getErrorLogger();
	}

	@Override
	public MethodHeader<E> getMethodHeader()
	{
		return methodHeader;
	}

	@Override
	public TypeCapture<E> getTypeCapture()
	{
		return typeCapture;
	}
}
