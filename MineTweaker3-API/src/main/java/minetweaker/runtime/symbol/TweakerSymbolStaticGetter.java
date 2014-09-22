/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import java.lang.reflect.Method;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.runtime.IAny;

/**
 *
 * @author Stan
 */
public class TweakerSymbolStaticGetter implements ITweakerSymbol {
	private final Method method;
	private final IAny value;
	
	public TweakerSymbolStaticGetter(Method method, IAny value) {
		this.method = method;
		this.value = value;
	}

	@Override
	public IZenSymbol convert(IScopeGlobal scope) {
		
	}

	@Override
	public IAny eval() {
		return value;
	}
}
