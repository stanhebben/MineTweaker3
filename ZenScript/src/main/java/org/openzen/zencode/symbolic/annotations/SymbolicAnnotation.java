/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.annotations;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolicAnnotation<E extends IPartialExpression<E>>
{
	public static <ES extends IPartialExpression<ES>>
		 List<SymbolicAnnotation<ES>> compileAll(List<ParsedAnnotation> annotations, IDefinitionScope<ES> scope)
	{
		List<SymbolicAnnotation<ES>> result = new ArrayList<SymbolicAnnotation<ES>>();
		for (ParsedAnnotation annotation : annotations) {
			result.add(annotation.compile(scope));
		}
		return result;
	}
	
	private final CodePosition position;
	private TypeInstance<E> type;
	private IMethod<E> constructor;
	private List<E> arguments;
	
	public SymbolicAnnotation(CodePosition position, TypeInstance<E> type, IMethod<E> constructor, List<E> arguments)
	{
		this.position = position;
		this.type = type;
		this.constructor = constructor;
		this.arguments = arguments;
	}
	
	public void validate()
	{
		for (E argument : arguments) {
			argument.validate();
		}
		
		constructor.validateCall(position, type.getScope().getConstantEnvironment(), arguments);
	}
}
