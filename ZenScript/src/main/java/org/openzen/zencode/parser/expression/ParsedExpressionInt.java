/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyLong;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;
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
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, IGenericType<E> predictedType)
	{
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		TypeRegistry<E> types = scope.getTypeCompiler();
		
		if (predictedType != null) {
			if (predictedType.equals(types.byte_))
				return compiler.constantByte(getPosition(), scope, (byte) value);
			else if (predictedType.equals(types.ubyte))
				return compiler.constantUByte(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.short_))
				return compiler.constantShort(getPosition(), scope, (short) value);
			else if (predictedType.equals(types.ushort))
				return compiler.constantUShort(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.int_))
				return compiler.constantInt(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.uint))
				return compiler.constantUInt(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.long_))
				return compiler.constantLong(getPosition(), scope, value);
			else if (predictedType.equals(types.ulong))
				return compiler.constantULong(getPosition(), scope, value);
			else if (predictedType.equals(types.float_))
				return compiler.constantFloat(getPosition(), scope, value);
			else if (predictedType.equals(types.double_))
				return compiler.constantDouble(getPosition(), scope, value);
			else if (predictedType.equals(types.char_))
				return compiler.constantChar(getPosition(), scope, (int) value);
			else if (predictedType.equals(types.string))
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
