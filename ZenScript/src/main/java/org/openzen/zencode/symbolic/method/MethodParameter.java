/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.method;

import java.util.Map;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.parser.elements.ParsedFunctionParameter;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class MethodParameter<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	private final ParsedFunctionParameter source;
	
	private final CodePosition position;
	private final String name;
	private final T type;
	private E defaultValue;
	private SymbolLocal<E, T> local;

	public MethodParameter(CodePosition position, String name, T type, E defaultValue)
	{
		source = null;
		
		this.position = position;
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public MethodParameter(ParsedFunctionParameter source, IDefinitionScope<E, T> scope)
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

	public T getType()
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

	public SymbolLocal<E, T> getLocal()
	{
		if (local == null)
			local = new SymbolLocal<E, T>(type, false);

		return local;
	}
	
	public void completeContents(IMethodScope<E, T> scope)
	{
		if (source == null)
			return;
		
		if (source.getDefaultValue() != null)
			defaultValue = source.getDefaultValue().compile(scope, type);
	}
	
	public void validate(IDefinitionScope<E, T> scope)
	{
		ITypeCompiler<E, T> types = type.getScope().getTypeCompiler();
		ICodeErrorLogger<E, T> errorLogger = type.getScope().getErrorLogger();
		
		if (type.equals(types.getVoid(scope)))
			errorLogger.errorVoidParameter(position, name);
	}
	
	public MethodParameter<E, T> instance(
			IDefinitionScope<E, T> scope,
			Map<GenericParameter<E, T>, T> genericParameters)
	{
		T instancedType = type.instance(scope);
		if (instancedType == type)
			return this;
		else
			return new MethodParameter<E, T>(position, name, type, defaultValue);
	}
}
