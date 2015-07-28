/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.definition;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.parser.definition.IParsedDefinition;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.DefinitionScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.TypeDefinition;
import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 * @param <E>
 */
public abstract class AbstractSymbolicDefinition<E extends IPartialExpression<E>>
	implements ISymbolicDefinition<E>
{
	private final IParsedDefinition source;
	
	private final int modifiers;
	private final IDefinitionScope<E> definitionScope;
	private final List<GenericParameter<E>> genericParameters;
	private final TypeDefinition<E> typeDefinition;
	
	private List<SymbolicAnnotation<E>> annotations;
	
	public AbstractSymbolicDefinition(IParsedDefinition source, IModuleScope<E> scope, boolean isStruct, boolean isInterface)
	{
		this.source = source;
		
		modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
		typeDefinition = new TypeDefinition<>(isStruct, isInterface);
		definitionScope = new DefinitionScope<>(scope, this, typeDefinition.getSelfInstance());
		genericParameters = GenericParameter.compile(source.getGenericParameters(), scope);
		typeDefinition.setTypeVariables(genericParameters);
	}
	
	public AbstractSymbolicDefinition(int modifiers, List<SymbolicAnnotation<E>> annotations, IModuleScope<E> scope, boolean isStruct, boolean isInterface)
	{
		this.source = null;
		
		this.modifiers = modifiers;
		this.annotations = annotations;
		typeDefinition = new TypeDefinition<>(isStruct, isInterface);
		definitionScope = new DefinitionScope<>(scope, this, typeDefinition.getSelfInstance());
		genericParameters = Collections.emptyList();
	}
	
	public IDefinitionScope<E> getScope()
	{
		return definitionScope;
	}
	
	public TypeDefinition<E> getDefinition()
	{
		return typeDefinition;
	}
	
	@Override
	public int getModifiers()
	{
		return modifiers;
	}
	
	@Override
	public List<SymbolicAnnotation<E>> getAnnotations()
	{
		return annotations;
	}
	
	@Override
	public void compileMembers()
	{
		for (GenericParameter<E> parameter : genericParameters) {
			parameter.completeMembers(getScope());
		}
	}
	
	@Override
	public void compileMemberContents()
	{
		if (source != null)
			annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), getScope());
		
		for (GenericParameter<E> parameter : genericParameters) {
			parameter.completeContents(getScope());
		}
	}
	
	@Override
	public void validate()
	{
		for (SymbolicAnnotation<E> annotation : annotations) {
			annotation.validate();
		}
	}
	
	@Override
	public List<? extends ITypeVariable<E>> getTypeVariables()
	{
		return genericParameters;
	}
}
