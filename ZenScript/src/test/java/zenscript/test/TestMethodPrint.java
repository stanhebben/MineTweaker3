/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.test;

import java.util.Collections;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.test.expression.TestExpressionPrint;
import org.openzen.zencode.test.expression.TestExpression;
import org.openzen.zencode.test.type.TestType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class TestMethodPrint extends TestMethod
{
	private final TestEnvironment environment;
	
	public TestMethodPrint(TestEnvironment environment)
	{
		super(new MethodHeader<TestExpression, TestType>(
				environment.getTypeCompiler().getVoid(),
				Collections.singletonList(new MethodParameter<TestExpression, TestType>("value", environment.getTypeCompiler().getString(), null)),
				false), true);
		
		this.environment = environment;
	}

	@Override
	public TestExpression callStatic(CodePosition position, IMethodScope<TestExpression, TestType> scope, TestExpression... arguments)
	{
		return new TestExpressionPrint(position, scope, arguments[0].getValue().asString());
	}

	@Override
	public TestExpression callStaticNullable(CodePosition position, IMethodScope<TestExpression, TestType> scope, TestExpression argument)
	{
		return callStatic(position, scope, argument);
	}

	@Override
	public TestExpression callVirtual(CodePosition position, IMethodScope<TestExpression, TestType> scope, TestExpression target, TestExpression... arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
