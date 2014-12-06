/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import java.lang.reflect.Method;
import minetweaker.runtime.TweakerGlobalScope;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.java.method.JavaMethod;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.symbols.SymbolStaticGetter;

/**
 *
 * @author Stan
 */
public class TweakerSymbolStaticGetter implements ITweakerSymbol
{
	private final Method method;
	private final IAny value;
	
	public TweakerSymbolStaticGetter(Method method, IAny value)
	{
		this.method = method;
		this.value = value;
	}

	@Override
	public IZenSymbol<IJavaExpression, IJavaType> convert(TweakerGlobalScope scope)
	{
		return new SymbolStaticGetter<IJavaExpression, IJavaType>(JavaMethod.get(scope, method));
	}

	@Override
	public IAny eval()
	{
		return value;
	}
}
