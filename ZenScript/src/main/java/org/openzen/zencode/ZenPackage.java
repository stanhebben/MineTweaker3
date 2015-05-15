/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialImportable;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.Strings;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ZenPackage<E extends IPartialExpression<E>> implements IImportable<E>
{
	public static <ES extends IPartialExpression<ES>> ZenPackage<ES> makeRootPackage()
	{
		return new ZenPackage<ES>();
	}
	
	private final Map<String, IImportable<E>> definitions;
	
	private ZenPackage()
	{
		definitions = new HashMap<String, IImportable<E>>();
	}
	
	public IImportable<E> resolve(CodePosition position, ICodeErrorLogger<E> errorLogger, List<String> name, boolean wildcard)
	{
		IImportable<E> result = this;
		for (int i = 0 ; i < name.size(); i++) {
			String nameElement = name.get(i);
			
			IImportable<E> theResult = result.getSubDefinition(nameElement);
			if (theResult == null) {
				if (wildcard || i < name.size() - 1)
					errorLogger.errorCouldNotResolvePackage(position, Strings.join(name.subList(0, i + 1), "."));
				else
					errorLogger.errorCouldNotResolveSymbol(position, Strings.join(name, "."));
			}
		}
		return result;
	}
	
	public void put(CodePosition position, ICodeErrorLogger<E> errorLogger, String name, IImportable<E> value)
	{
		
	}
	
	public Collection<IImportable<E>> getAllDefinitions()
	{
		return definitions.values();
	}

	@Override
	public IImportable<E> getSubDefinition(String name)
	{
		return definitions.get(name);
	}

	@Override
	public IGenericType<E> toType(IModuleScope<E> scope, List<IGenericType<E>> genericTypes)
	{
		return null;
	}

	@Override
	public IPartialExpression<E> toPartialExpression(CodePosition position, IMethodScope<E> scope)
	{
		return new PartialImportable<E>(position, scope, this);
	}

	@Override
	public IZenSymbol<E> getMember(String name)
	{
		return null;
	}

	@Override
	public Collection<String> getSubDefinitionNames()
	{
		return definitions.keySet();
	}
}
