/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package stanhebben.zenscript.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.symbolic.field.IField;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ExpressionGetStaticField extends Expression
{
	private final IField field;
	
	public ExpressionGetStaticField(ZenPosition position, IScopeMethod scope, IField field)
	{
		super(position, scope);
		
		this.field = field;
	}

	@Override
	public void compile(boolean result, MethodOutput output)
	{
		if (result) {
			field.compileStaticGet(output);
		}
	}

	@Override
	public ZenType getType()
	{
		return field.getType();
	}
}
