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
import org.openzen.zencode.symbolic.scope.IScopeMethod;
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
	public E invalid(CodePosition position, IScopeMethod<E, T> scope);
	
	public E invalid(CodePosition position, IScopeMethod<E, T> scope, IZenType<E, T> valueType);
	
	public E constantNull(CodePosition position, IScopeMethod<E, T> scope);
	
	public E constantBool(CodePosition position, IScopeMethod<E, T> scope, boolean value);
	
	public E constantByte(CodePosition position, IScopeMethod<E, T> scope, byte value);
	
	public E constantUByte(CodePosition position, IScopeMethod<E, T> scope, int value);
	
	public E constantShort(CodePosition position, IScopeMethod<E, T> scope, short value);
	
	public E constantUShort(CodePosition position, IScopeMethod<E, T> scope, int value);
	
	public E constantInt(CodePosition position, IScopeMethod<E, T> scope, int value);
	
	public E constantUInt(CodePosition position, IScopeMethod<E, T> scope, int value);
	
	public E constantLong(CodePosition position, IScopeMethod<E, T> scope, long value);
	
	public E constantULong(CodePosition position, IScopeMethod<E, T> scope, long value);
	
	public E constantFloat(CodePosition position, IScopeMethod<E, T> scope, float value);
	
	public E constantDouble(CodePosition position, IScopeMethod<E, T> scope, double value);
	
	public E constantChar(CodePosition position, IScopeMethod<E, T> scope, int value);
	
	public E constantString(CodePosition position, IScopeMethod<E, T> scope, String value);
	
	public E constant(CodePosition position, IScopeMethod<E, T> scope, Object value);
	
	public E[] constants(CodePosition position, IScopeMethod<E, T> scope, Object[] values);
	
	public E localGet(CodePosition position, IScopeMethod<E, T> scope, SymbolLocal<E, T> local);
	
	public E localSet(CodePosition position, IScopeMethod<E, T> scope, SymbolLocal<E, T> local, E value);
	
	public E andAnd(CodePosition position, IScopeMethod<E, T> scope, E left, E right);
	
	public E orOr(CodePosition position, IScopeMethod<E, T> scope, E left, E right);
	
	public E compareGeneric(CodePosition position, IScopeMethod<E, T> scope, E value, CompareType comparator);
	
	public E ternary(CodePosition position, IScopeMethod<E, T> scope, E condition, E ifValue, E elseValue);
	
	public E notNull(CodePosition position, IScopeMethod<E, T> scope, E value);
	
	public E range(CodePosition position, IScopeMethod<E, T> scope, E from, E to);
	
	public E array(CodePosition position, IScopeMethod<E, T> scope, T arrayType, List<E> values);
	
	public E map(CodePosition position, IScopeMethod<E, T> scope, T mapType, List<E> keys, List<E> maps);
	
	public E constructNew(CodePosition position, IScopeMethod<E, T> scope, IZenType<E, T> type, IMethod<E, T> method, E[] arguments);
	
	public E functionExpression(CodePosition position, IScopeMethod<E, T> scope, MethodHeader<E, T> header, List<Statement<E, T>> statements);
	
	public E staticMethodValue(CodePosition position, IScopeMethod<E, T> scope, IMethod<E, T> method);
	
	public E anyNot(CodePosition position, IScopeMethod<E, T> scope, E value);
	
	public E anyNeg(CodePosition position, IScopeMethod<E, T> scope, E value);
	
	public E anyInvert(CodePosition position, IScopeMethod<E, T> scope, E value);
	
	public E anyAdd(CodePosition position, IScopeMethod<E, T> scope, E left, E right);
	
	public E anySub(CodePosition position, IScopeMethod<E, T> scope, E left, E right);
	
	public E anyCat(CodePosition position, IScopeMethod<E, T> scope, E left, E right);
	
	public E anyMul(CodePosition position, IScopeMethod<E, T> scope, E left, E right);
	
	public E anyDiv(CodePosition position, IScopeMethod<E, T> scope, E left, E right);
	
	public E anyMod(CodePosition position, IScopeMethod<E, T> scope, E left, E right);
	
	public E anyCastTo(CodePosition position, IScopeMethod<E, T> scope, E value, T type);
}
