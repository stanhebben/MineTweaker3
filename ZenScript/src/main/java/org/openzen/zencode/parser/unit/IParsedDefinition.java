/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.unit;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.generic.ParsedGenericParameter;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 */
public interface IParsedDefinition
{
	public <E extends IPartialExpression<E>> ISymbolicDefinition<E> compile(IModuleScope<E> scope);
	
	public List<IParsedModifier> getModifiers();
	
	public List<ParsedAnnotation> getAnnotations();
	
	public List<ParsedGenericParameter> getGenericParameters();
}
