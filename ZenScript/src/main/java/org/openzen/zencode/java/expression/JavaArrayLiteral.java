/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.Type;
import org.openzen.zencode.java.JavaCompileState;
import org.openzen.zencode.java.type.JavaTypeInfo;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyArray;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaArrayLiteral extends AbstractJavaExpression
{
	private final JavaCompileState compileState;
	private final List<IJavaExpression> values;
	private final IGenericType<IJavaExpression> type;
	
	public JavaArrayLiteral(
			CodePosition position,
			IMethodScope<IJavaExpression> scope,
			JavaCompileState compileState,
			IGenericType<IJavaExpression> type,
			List<IJavaExpression> values)
	{
		super(position, scope);
		
		this.compileState = compileState;
		this.type = type;
		this.values = values;
	}

	@Override
	public void compileValue(MethodOutput method)
	{
		JavaTypeInfo typeInfo = compileState.getTypeInfo(type);
		if (typeInfo.isArray()) {
			Type componentType = typeInfo.getArrayBaseType().toASMType();

			method.constant(values.size());
			method.newArray(componentType);

			for (int i = 0; i < values.size(); i++) {
				method.dup();
				method.constant(i);
				values.get(i).compileValue(method);
				method.arrayStore(componentType);
			}
		} else if (typeInfo.isList()) {
			method.construct(List.class);

			for (int i = 0; i < values.size(); i++) {
				method.dup();
				values.get(i).compileValue(method);
				method.invokeInterface(Type.getInternalName(List.class), "add", "(Ljava/lang/Object;)Z");
			}
		} else {
			throw new RuntimeException("Array literal type is not a valid array");
		}
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		for (IJavaExpression value : values) {
			value.compileStatement(method);
		}
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return type;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		List<IAny> ctValues = new ArrayList<>();
		for (IJavaExpression value : values) {
			IAny ctValue = value.getCompileTimeValue();
			if (ctValue == null)
				return null;
			
			ctValues.add(ctValue);
		}
		
		return new AnyArray(ctValues);
	}

	@Override
	public void validate()
	{
		
	}
}
