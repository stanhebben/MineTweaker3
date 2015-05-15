/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.parser.definition.ParsedFunctionParameter;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
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
	private final IGenericType<E> type;
	private E defaultValue;
	private LocalSymbol<E> local;

	public MethodParameter(CodePosition position, String name, IGenericType<E> type, E defaultValue)
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

	public IGenericType<E> getType()
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

	public LocalSymbol<E> getLocal()
	{
		if (local == null)
			local = new LocalSymbol<E>(type, false);

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
		TypeRegistry<E> types = scope.getTypeCompiler();
		ICodeErrorLogger<E> errorLogger = scope.getErrorLogger();
		
		if (type.equals(types.void_))
			errorLogger.errorVoidParameter(position, name);
	}
	
	public MethodParameter<E> instance(TypeCapture<E> capture)
	{
		IGenericType<E> instancedType = type.instance(capture);
		if (instancedType == type)
			return this;
		else
			return new MethodParameter<>(position, name, instancedType, defaultValue);
	}
}
