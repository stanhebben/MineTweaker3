/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ExpressionSetStaticField extends Expression
{
	private final IField field;
	private final Expression value;
	
	public ExpressionSetStaticField(CodePosition position, IScopeMethod scope, IField field, Expression value)
	{
		super(position, scope);
		
		this.field = field;
		this.value = value;
	}
	
	@Override
	public void compile(boolean result, MethodOutput output)
	{
		value.compile(true, output);
		
		if (result)
			output.dup(field.getType().isLarge());
		
		field.compileStaticSet(output);
	}

	@Override
	public ZenType getType()
	{
		return field.getType();
	}
}
