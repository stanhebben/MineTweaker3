/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaCompareGeneric extends AbstractJavaExpression
{
	private final IJavaExpression value;
	private final CompareType compareType;
	
	public JavaCompareGeneric(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value, CompareType compareType)
	{
		super(position, scope);
		
		this.value = value;
		this.compareType = compareType;
	}
	
	@Override
	public void compileValue(MethodOutput method)
	{
		value.compileValue(method);
		
		Label onIf = new Label();
		Label after = new Label();
		
		switch (compareType) {
			case LT:
				method.ifLT(onIf);
				break;
			case GT:
				method.ifGT(onIf);
				break;
			case EQ:
				method.ifEQ(onIf);
				break;
			case NE:
				method.ifNE(onIf);
				break;
			case LE:
				method.ifLE(onIf);
				break;
			case GE:
				method.ifGE(onIf);
				break;
			default:
				throw new AssertionError("Unknown case value:" + compareType);
		}
		
		method.constant(0);
		method.goTo(after);
		method.label(onIf);
		method.constant(1);
		method.label(after);
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		value.compileStatement(method);
	}
	
	@Override
	public void compileIf(Label onIf, MethodOutput method)
	{
		value.compileValue(method);
		
		switch (compareType) {
			case LT:
				method.ifLT(onIf);
				break;
			case GT:
				method.ifGT(onIf);
				break;
			case EQ:
				method.ifEQ(onIf);
				break;
			case NE:
				method.ifNE(onIf);
				break;
			case LE:
				method.ifLE(onIf);
				break;
			case GE:
				method.ifGE(onIf);
				break;
			default:
				throw new AssertionError("Unknown case value:" + compareType);
		}
	}
	
	@Override
	public void compileElse(Label onElse, MethodOutput method)
	{
		value.compileValue(method);
		
		switch (compareType) {
			case LT:
				method.ifGE(onElse);
				break;
			case GT:
				method.ifLE(onElse);
				break;
			case EQ:
				method.ifNE(onElse);
				break;
			case NE:
				method.ifEQ(onElse);
				break;
			case LE:
				method.ifGT(onElse);
				break;
			case GE:
				method.ifLT(onElse);
				break;
			default:
				throw new AssertionError("Unknown case value:" + compareType);
		}
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().bool;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		IAny valueCT = value.getCompileTimeValue();
		if (valueCT == null)
			return null;
		
		int iValue = valueCT.asInt();
		return AnyBool.valueOf(compareType.evaluate(iValue));
	}

	@Override
	public void validate()
	{
		value.validate();
	}
}
