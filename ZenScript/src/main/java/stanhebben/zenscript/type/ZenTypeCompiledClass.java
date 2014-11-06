/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package stanhebben.zenscript.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.unit.SymbolicClass;

/**
 *
 * @author Stan
 */
public class ZenTypeCompiledClass extends ZenType
{
	private final SymbolicClass forClass;
	
	public ZenTypeCompiledClass(IScopeGlobal scope, SymbolicClass forClass)
	{
		super(scope);
		
		this.forClass = forClass;
	}
}
