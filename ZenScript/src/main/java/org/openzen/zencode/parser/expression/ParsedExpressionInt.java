/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyLong;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionInt extends ParsedExpression
{
	private final long value;

	public ParsedExpressionInt(CodePosition position, long value)
	{
		super(position);

		this.value = value;
	}

	@Override
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, TypeInstance<E> predictedType)
	{
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		ITypeCompiler<E> types = scope.getTypeCompiler();
		
		if (predictedType != null) {
			if (predictedType.equals(types.getByte(scope)))
				return compiler.constantByte(getPosition(), scope, (byte) value);
			else if (predictedType.equals(types.getUByte(scope)))
				return compiler.constantUByte(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.getShort(scope)))
				return compiler.constantShort(getPosition(), scope, (short) value);
			else if (predictedType.equals(types.getUShort(scope)))
				return compiler.constantUShort(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.getInt(scope)))
				return compiler.constantInt(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.getUInt(scope)))
				return compiler.constantUInt(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.getLong(scope)))
				return compiler.constantLong(getPosition(), scope, value);
			else if (predictedType.equals(types.getULong(scope)))
				return compiler.constantULong(getPosition(), scope, value);
			else if (predictedType.equals(types.getFloat(scope)))
				return compiler.constantFloat(getPosition(), scope, value);
			else if (predictedType.equals(types.getDouble(scope)))
				return compiler.constantDouble(getPosition(), scope, value);
			else if (predictedType.equals(types.getChar(scope)))
				return compiler.constantChar(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.getString(scope)))
				return compiler.constantString(getPosition(), scope, Long.toString(value));
		}
		
		IPartialExpression<E> result;
		if (value <= Integer.MAX_VALUE)
			result = compiler.constantInt(getPosition(), scope, (int) value);
		else
			result = compiler.constantLong(getPosition(), scope, value);
		
		if (predictedType != null)
			result = result.cast(getPosition(), predictedType);

		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		return new AnyLong(value);
	}
}
