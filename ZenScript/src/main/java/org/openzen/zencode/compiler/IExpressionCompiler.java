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
import org.openzen.zencode.symbolic.type.TypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IExpressionCompiler<E extends IPartialExpression<E>>
{
	public E invalid(CodePosition position, IMethodScope<E> scope);
	
	public E invalid(CodePosition position, IMethodScope<E> scope, TypeInstance<E> valueType);
	
	public E constantNull(CodePosition position, IMethodScope<E> scope);
	
	public E constantBool(CodePosition position, IMethodScope<E> scope, boolean value);
	
	public E constantByte(CodePosition position, IMethodScope<E> scope, byte value);
	
	public E constantUByte(CodePosition position, IMethodScope<E> scope, int value);
	
	public E constantShort(CodePosition position, IMethodScope<E> scope, short value);
	
	public E constantUShort(CodePosition position, IMethodScope<E> scope, int value);
	
	public E constantInt(CodePosition position, IMethodScope<E> scope, int value);
	
	public E constantUInt(CodePosition position, IMethodScope<E> scope, int value);
	
	public E constantLong(CodePosition position, IMethodScope<E> scope, long value);
	
	public E constantULong(CodePosition position, IMethodScope<E> scope, long value);
	
	public E constantFloat(CodePosition position, IMethodScope<E> scope, float value);
	
	public E constantDouble(CodePosition position, IMethodScope<E> scope, double value);
	
	public E constantChar(CodePosition position, IMethodScope<E> scope, int value);
	
	public E constantString(CodePosition position, IMethodScope<E> scope, String value);
	
	public E constant(CodePosition position, IMethodScope<E> scope, Object value);
	
	public List<E> constants(CodePosition position, IMethodScope<E> scope, Object[] values);
	
	public E localGet(CodePosition position, IMethodScope<E> scope, SymbolLocal<E> local);
	
	public E localSet(CodePosition position, IMethodScope<E> scope, SymbolLocal<E> local, E value);
	
	public E andAnd(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orOr(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareGeneric(CodePosition position, IMethodScope<E> scope, E value, CompareType comparator);
	
	public E ternary(CodePosition position, IMethodScope<E> scope, E condition, E ifValue, E elseValue);
	
	public E notNull(CodePosition position, IMethodScope<E> scope, E value);
	
	public E range(CodePosition position, IMethodScope<E> scope, E from, E to);
	
	public E array(CodePosition position, IMethodScope<E> scope, TypeInstance<E> arrayType, List<E> values);
	
	public E map(CodePosition position, IMethodScope<E> scope, TypeInstance<E> mapType, List<E> keys, List<E> maps);
	
	public E constructNew(CodePosition position, IMethodScope<E> scope, TypeInstance<E> type, IMethod<E> method, List<E> arguments);
	
	public E functionExpression(CodePosition position, IMethodScope<E> scope, MethodHeader<E> header, List<Statement<E>> statements);
	
	public E staticMethodValue(CodePosition position, IMethodScope<E> scope, IMethod<E> method);
	
	public E anyNot(CodePosition position, IMethodScope<E> scope, E value);
	
	public E anyNeg(CodePosition position, IMethodScope<E> scope, E value);
	
	public E anyInvert(CodePosition position, IMethodScope<E> scope, E value);
	
	public E anyAdd(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E anySub(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E anyCat(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E anyMul(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E anyDiv(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E anyMod(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E anyCastTo(CodePosition position, IMethodScope<E> scope, E value, TypeInstance<E> type);
}
