/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.compiler;

import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface IExpressionCompiler<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public E invalid(CodePosition position, IMethodScope<E, T> scope);
	
	public E invalid(CodePosition position, IMethodScope<E, T> scope, IZenType<E, T> valueType);
	
	public E constantNull(CodePosition position, IMethodScope<E, T> scope);
	
	public E constantBool(CodePosition position, IMethodScope<E, T> scope, boolean value);
	
	public E constantByte(CodePosition position, IMethodScope<E, T> scope, byte value);
	
	public E constantUByte(CodePosition position, IMethodScope<E, T> scope, int value);
	
	public E constantShort(CodePosition position, IMethodScope<E, T> scope, short value);
	
	public E constantUShort(CodePosition position, IMethodScope<E, T> scope, int value);
	
	public E constantInt(CodePosition position, IMethodScope<E, T> scope, int value);
	
	public E constantUInt(CodePosition position, IMethodScope<E, T> scope, int value);
	
	public E constantLong(CodePosition position, IMethodScope<E, T> scope, long value);
	
	public E constantULong(CodePosition position, IMethodScope<E, T> scope, long value);
	
	public E constantFloat(CodePosition position, IMethodScope<E, T> scope, float value);
	
	public E constantDouble(CodePosition position, IMethodScope<E, T> scope, double value);
	
	public E constantChar(CodePosition position, IMethodScope<E, T> scope, int value);
	
	public E constantString(CodePosition position, IMethodScope<E, T> scope, String value);
	
	public E constant(CodePosition position, IMethodScope<E, T> scope, Object value);
	
	public List<E> constants(CodePosition position, IMethodScope<E, T> scope, Object[] values);
	
	public E localGet(CodePosition position, IMethodScope<E, T> scope, SymbolLocal<E, T> local);
	
	public E localSet(CodePosition position, IMethodScope<E, T> scope, SymbolLocal<E, T> local, E value);
	
	public E andAnd(CodePosition position, IMethodScope<E, T> scope, E left, E right);
	
	public E orOr(CodePosition position, IMethodScope<E, T> scope, E left, E right);
	
	public E compareGeneric(CodePosition position, IMethodScope<E, T> scope, E value, CompareType comparator);
	
	public E ternary(CodePosition position, IMethodScope<E, T> scope, E condition, E ifValue, E elseValue);
	
	public E notNull(CodePosition position, IMethodScope<E, T> scope, E value);
	
	public E range(CodePosition position, IMethodScope<E, T> scope, E from, E to);
	
	public E array(CodePosition position, IMethodScope<E, T> scope, T arrayType, List<E> values);
	
	public E map(CodePosition position, IMethodScope<E, T> scope, T mapType, List<E> keys, List<E> maps);
	
	public E constructNew(CodePosition position, IMethodScope<E, T> scope, IZenType<E, T> type, IMethod<E, T> method, List<E> arguments);
	
	public E functionExpression(CodePosition position, IMethodScope<E, T> scope, MethodHeader<E, T> header, List<Statement<E, T>> statements);
	
	public E staticMethodValue(CodePosition position, IMethodScope<E, T> scope, IMethod<E, T> method);
	
	public E anyNot(CodePosition position, IMethodScope<E, T> scope, E value);
	
	public E anyNeg(CodePosition position, IMethodScope<E, T> scope, E value);
	
	public E anyInvert(CodePosition position, IMethodScope<E, T> scope, E value);
	
	public E anyAdd(CodePosition position, IMethodScope<E, T> scope, E left, E right);
	
	public E anySub(CodePosition position, IMethodScope<E, T> scope, E left, E right);
	
	public E anyCat(CodePosition position, IMethodScope<E, T> scope, E left, E right);
	
	public E anyMul(CodePosition position, IMethodScope<E, T> scope, E left, E right);
	
	public E anyDiv(CodePosition position, IMethodScope<E, T> scope, E left, E right);
	
	public E anyMod(CodePosition position, IMethodScope<E, T> scope, E left, E right);
	
	public E anyCastTo(CodePosition position, IMethodScope<E, T> scope, E value, T type);
}
