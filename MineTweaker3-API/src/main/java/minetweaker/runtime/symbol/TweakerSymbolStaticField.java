/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import java.lang.reflect.Field;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.field.JavaField;
import org.openzen.zencode.symbolic.symbols.SymbolStaticField;

/**
 *
 * @author Stan
 */
public class TweakerSymbolStaticField implements ITweakerSymbol
{
	private final Field field;
	private final IAny value;

	public TweakerSymbolStaticField(Field field, IAny value)
	{
		this.field = field;
		this.value = value;
	}

	@Override
	public IZenSymbol convert(IScopeGlobal scope)
	{
		return new SymbolStaticField(new JavaField(field, scope.getTypes()));
	}

	@Override
	public IAny eval()
	{
		return value;
	}
}
