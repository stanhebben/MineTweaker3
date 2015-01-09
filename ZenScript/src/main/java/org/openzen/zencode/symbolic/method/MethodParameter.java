/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.parser.elements.ParsedFunctionParameter;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class MethodParameter<E extends IPartialExpression<E>>
{
	private final ParsedFunctionParameter source;
	
	private final CodePosition position;
	private final String name;
	private final TypeInstance<E> type;
	private E defaultValue;
	private SymbolLocal<E> local;

	public MethodParameter(CodePosition position, String name, TypeInstance<E> type, E defaultValue)
	{
		source = null;
		
		this.position = position;
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public MethodParameter(ParsedFunctionParameter source, IDefinitionScope<E> scope)
	{
		this.source = source;
		
		position = source.getPosition();
		name = source.getName();
		type = source.getType().compile(scope);
	}

	public String getName()
	{
		return name;
	}

	public TypeInstance<E> getType()
	{
		return type;
	}
	
	public boolean hasDefaultValue()
	{
		return defaultValue != null || (source != null && source.getDefaultValue() != null);
	}

	public E getDefaultValue()
	{
		return defaultValue;
	}

	public SymbolLocal<E> getLocal()
	{
		if (local == null)
			local = new SymbolLocal<E>(type, false);

		return local;
	}
	
	public void completeContents(IMethodScope<E> scope)
	{
		if (source == null)
			return;
		
		if (source.getDefaultValue() != null)
			defaultValue = source.getDefaultValue().compile(scope, type);
	}
	
	public void validate(IDefinitionScope<E> scope)
	{
		ITypeCompiler<E> types = type.getScope().getTypeCompiler();
		ICodeErrorLogger<E> errorLogger = type.getScope().getErrorLogger();
		
		if (type.equals(types.getVoid(scope)))
			errorLogger.errorVoidParameter(position, name);
	}
	
	public MethodParameter<E> instance(
			IDefinitionScope<E> scope,
			TypeCapture<E> typeCapture)
	{
		TypeInstance<E> instancedType = type.instance(scope, typeCapture);
		if (instancedType == type)
			return this;
		else
			return new MethodParameter<E>(position, name, type, defaultValue);
	}
}
