/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.runtime.IAny;

/**
 *
 * @author Stan
 */
public class TweakerSymbolStaticMethod implements ITweakerSymbol {
	private final Method method;
	
	public TweakerSymbolStaticMethod(Method method) {
		if ((method.getModifiers() & Modifier.STATIC) == 0)
			throw new IllegalArgumentException("Method must be static");
		
		this.method = method;
	}

	@Override
	public IZenSymbol convert(IScopeGlobal scope) {
		
	}

	@Override
	public IAny eval() {
		return null;
	}
}
