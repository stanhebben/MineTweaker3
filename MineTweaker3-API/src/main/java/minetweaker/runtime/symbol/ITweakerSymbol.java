/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.runtime.IAny;

/**
 *
 * @author Stan
 */
public interface ITweakerSymbol {
	public IZenSymbol convert(IScopeGlobal scope);
	
	public IAny eval();
}
