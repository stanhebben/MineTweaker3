/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import java.lang.reflect.Field;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.runtime.IAny;

/**
 *
 * @author Stan
 */
public class TweakerSymbolStaticField implements ITweakerSymbol {
	private final Field field;
	private final IAny value;
	
	public TweakerSymbolStaticField(Field field, IAny value) {
		this.field = field;
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
