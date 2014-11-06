/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ExpressionGetStaticField extends Expression
{
	private final IField field;
	
	public ExpressionGetStaticField(CodePosition position, IScopeMethod scope, IField field)
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
