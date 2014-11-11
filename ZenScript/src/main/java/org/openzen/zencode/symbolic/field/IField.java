/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.field;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;

/**
 *
 * @author Stan
 */
public interface IField
{
	public ZenType getType();
	
	public boolean isFinal();
	
	public boolean isStatic();
	
	public void compileStaticGet(MethodOutput output);
	
	public void compileStaticSet(MethodOutput output);
	
	public void compileInstanceGet(MethodOutput output);
	
	public void compileInstanceSet(MethodOutput output);
	
	public Expression makeStaticGetExpression(CodePosition position, IScopeMethod scope);
	
	public Expression makeStaticSetExpression(CodePosition position, IScopeMethod scope, Expression value);
	
	public Expression makeInstanceGetExpression(CodePosition position, IScopeMethod scope, Expression target);
	
	public Expression makeInstanceSetExpression(CodePosition position, IScopeMethod scope, Expression target, Expression value);
}
