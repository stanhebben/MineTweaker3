/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class ExpressionSetInstanceField extends Expression
{
	private final Expression target;
	private final IField field;
	private final Expression value;
	
	public ExpressionSetInstanceField(CodePosition position, IScopeMethod scope, Expression target, IField field, Expression value)
	{
		super(position, scope);
		
		this.target = target;
		this.field = field;
		this.value = value;
	}

	@Override
	public void compile(boolean result, MethodOutput output)
	{
		if (result) {
			value.compile(true, output);
			target.compile(true, output);
			output.dupX1(value.getType().isLarge());
			field.compileInstanceSet(output);
		} else {
			target.compile(true, output);
			value.compile(true, output);
			field.compileInstanceSet(output);
		}
	}

	@Override
	public ZenType getType()
	{
		return value.getType();
	}
}
