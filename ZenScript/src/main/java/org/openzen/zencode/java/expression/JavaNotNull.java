/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaNotNull extends AbstractJavaExpression
{
	private final IJavaExpression value;
	
	public JavaNotNull(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		super(position, scope);
		
		this.value = value;
	}

	@Override
	public void compileValue(MethodOutput method)
	{
		value.compileValue(method);
		
		Label labelElse = new Label();
		Label labelAfter = new Label();

		method.ifNull(labelElse);
		method.iConst1();
		method.goTo(labelAfter);
		method.label(labelElse);
		method.iConst0();
		method.label(labelAfter);
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		value.compileStatement(method);
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().bool;
	}
	
	@Override
	public void compileIf(Label onIf, MethodOutput output)
	{
		compileValue(output);
		output.ifNonNull(onIf);
	}

	@Override
	public void compileElse(Label onElse, MethodOutput output)
	{
		compileValue(output);
		output.ifNull(onElse);
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return value.getCompileTimeValue();
	}

	@Override
	public void validate()
	{
		if (value.getType().equals(getScope().getTypeCompiler().null_))
			getScope().getErrorLogger().errorCannotBeNull(getPosition());
	}
}
