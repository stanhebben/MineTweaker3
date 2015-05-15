/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IModuleScope;

/**
 * ZenCode symbols need a scope in order to exist. However, global symbols have
 * to be registered outside a compilation scope. Tweaker symbols are there to
 * fill that gap - they are converted into ZenCode symbols when compilation is
 * being prepared.
 *
 * @author Stan Hebben
 */
public interface ITweakerSymbol
{
	public IZenSymbol<IJavaExpression> convert(IModuleScope<IJavaExpression> scope);

	public IAny eval();
}
