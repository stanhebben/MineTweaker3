/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyLong;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;
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
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IScopeMethod<E, T> scope, T predictedType)
	{
		IExpressionCompiler<E, T> compiler = scope.getExpressionCompiler();
		ITypeCompiler<E, T> types = scope.getTypes();

		if (predictedType == types.getByte())
			return compiler.constantByte(getPosition(), scope, (byte) value);
		else if (predictedType == types.getUByte())
			return compiler.constantUByte(getPosition(), scope, (int) value);
		else if (predictedType == types.getShort())
			return compiler.constantShort(getPosition(), scope, (short) value);
		else if (predictedType == types.getUShort())
			return compiler.constantUShort(getPosition(), scope, (int) value);
		else if (predictedType == types.getInt())
			return compiler.constantInt(getPosition(), scope, (int) value);
		else if (predictedType == types.getUInt())
			return compiler.constantUInt(getPosition(), scope, (int) value);
		else if (predictedType == types.getLong())
			return compiler.constantLong(getPosition(), scope, value);
		else if (predictedType == types.getULong())
			return compiler.constantULong(getPosition(), scope, value);
		else if (predictedType == types.getFloat())
			return compiler.constantFloat(getPosition(), scope, value);
		else if (predictedType == types.getDouble())
			return compiler.constantDouble(getPosition(), scope, value);
		else if (predictedType == types.getChar())
			return compiler.constantChar(getPosition(), scope, (int) value);
		else if (predictedType == types.getString())
			return compiler.constantString(getPosition(), scope, Long.toString(value));
		
		IPartialExpression<E, T> result;
		if (value <= Integer.MAX_VALUE)
			result = compiler.constantInt(getPosition(), scope, (int) value);
		else
			result = compiler.constantLong(getPosition(), scope, value);
		
		if (predictedType != null)
			result = result.cast(getPosition(), predictedType);

		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		return new AnyLong(value);
	}
}
