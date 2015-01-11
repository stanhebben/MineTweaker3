/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import java.lang.reflect.Field;
import minetweaker.runtime.TweakerGlobalScope;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.java.field.JavaField;
import org.openzen.zencode.symbolic.symbols.StaticFieldSymbol;

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
	public IZenSymbol<IJavaExpression> convert(TweakerGlobalScope scope)
	{
		return new StaticFieldSymbol<IJavaExpression>(new JavaField(field, scope.getTypeCompiler()));
	}

	@Override
	public IAny eval()
	{
		return value;
	}
}
