/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.compiler;

import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.ICallerMember;
import org.openzen.zencode.symbolic.member.IGetterMember;
import org.openzen.zencode.symbolic.member.IMethodMember;
import org.openzen.zencode.symbolic.member.IOperatorMember;
import org.openzen.zencode.symbolic.member.ISetterMember;
import org.openzen.zencode.symbolic.member.definition.ConstructorMember;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IExpressionCompiler<E extends IPartialExpression<E>>
{
	public E invalid(CodePosition position, IMethodScope<E> scope);
	
	public E invalid(CodePosition position, IMethodScope<E> scope, IGenericType<E> valueType);
	
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
	
	public E localGet(CodePosition position, IMethodScope<E> scope, LocalSymbol<E> local);
	
	public E localSet(CodePosition position, IMethodScope<E> scope, LocalSymbol<E> local, E value);
	
	public E andAnd(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orOr(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareGeneric(CodePosition position, IMethodScope<E> scope, E value, CompareType comparator);
	
	public E ternary(CodePosition position, IMethodScope<E> scope, E condition, E ifValue, E elseValue);
	
	public E notNull(CodePosition position, IMethodScope<E> scope, E value);
	
	public E range(CodePosition position, IMethodScope<E> scope, E from, E to);
	
	public E array(CodePosition position, IMethodScope<E> scope, IGenericType<E> arrayType, List<E> values);
	
	public E map(CodePosition position, IMethodScope<E> scope, IGenericType<E> mapType, List<E> keys, List<E> maps);
	
	public E constructNew(CodePosition position, IMethodScope<E> scope, TypeInstance<E> type, ConstructorMember<E> constructor, List<E> arguments);
	
	public E constructNewGeneric(CodePosition position, IMethodScope<E> scope, TypeInstance<E> type, ITypeVariable<E> typeVariable, List<E> arguments);
	
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
	
	public E virtualGetter(CodePosition position, IMethodScope<E> scope, E instance, IGetterMember<E> getter);
	
	public E virtualSetter(CodePosition position, IMethodScope<E> scope, E instance, ISetterMember<E> setter, E value);
	
	public E staticGetter(CodePosition position, IMethodScope<E> scope, IGetterMember<E> getter);
	
	public E staticSetter(CodePosition position, IMethodScope<E> scope, ISetterMember<E> setter, E value);
	
	public E callOperator(CodePosition position, IMethodScope<E> scope, IOperatorMember<E> member, E instance, List<E> arguments);
	
	public E callVirtualCaller(CodePosition position, IMethodScope<E> scope, ICallerMember<E> member, E instance, List<E> arguments);
	
	public E callStaticCaller(CodePosition position, IMethodScope<E> scope, ICallerMember<E> member, List<E> arguments);
	
	public E callVirtualMethod(CodePosition position, IMethodScope<E> scope, IMethodMember<E> member, E instance, List<E> arguments);
	
	public E callStaticMethod(CodePosition position, IMethodScope<E> scope, IMethodMember<E> member, List<E> arguments);
	
	public E callFunction(CodePosition position, IMethodScope<E> scope, E value, List<E> arguments);
	
	public E notBool(CodePosition position, IMethodScope<E> scope, E value);
	
	public E andBool(CodePosition position, IMethodScope<E> scope, E a, E b);
	
	public E orBool(CodePosition position, IMethodScope<E> scope, E a, E b);
	
	public E xorBool(CodePosition position, IMethodScope<E> scope, E a, E b);
	
	public E equalsBool(CodePosition position, IMethodScope<E> scope, E a, E b);
	
	public E notEqualsBool(CodePosition position, IMethodScope<E> scope, E a, E b);
	
	public E negByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E invertByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E andByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E xorByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shlByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shrByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareByte(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E invertUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E andUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E xorUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shlUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shrUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E ushrUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareUByte(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E invertShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E negShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E andShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E xorShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shlShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shrShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E ushrShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareShort(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E invertUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shlUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shrUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E andUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E xorUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareUShort(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E negInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E invertInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addInt(CodePosition positino, IMethodScope<E> scope, E left, E right);
	
	public E subInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E andInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E xorInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shlInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shrInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E ushrInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareInt(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E invertUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E andUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E xorUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shlUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shrUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E ushrUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareUInt(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E negLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E invertLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E andLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E xorLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shlLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shrLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E ushrLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareLong(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E invertULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E andULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E orULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E xorULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shlULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E shrULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E ushrULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareULong(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E negFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addFloat(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subFloat(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulFloat(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divFloat(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modFloat(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareFloat(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E negDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E addDouble(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E subDouble(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E mulDouble(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E divDouble(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E modDouble(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E compareDouble(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E compareChar(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E catStringBool(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringUByte(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringUShort(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringUInt(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringLong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringULong(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringFloat(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringDouble(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringChar(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E catStringString(CodePosition position, IMethodScope<E> scope, E left, E right);
	
	public E indexString(CodePosition position, IMethodScope<E> scope, E value, E index);
	
	public E compareString(CodePosition position, IMethodScope<E> scope, CompareType compareType, E left, E right);
	
	public E boolToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E byteToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ubyteToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E shortToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ushortToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E intToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E uintToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E longToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E ulongToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToDouble(CodePosition position, IMethodScope<E> scope, E value);
	
	public E floatToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToLong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToULong(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToFloat(CodePosition position, IMethodScope<E> scope, E value);
	
	public E doubleToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E charToByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E charToUByte(CodePosition position, IMethodScope<E> scope, E value);
	
	public E charToShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E charToUShort(CodePosition position, IMethodScope<E> scope, E value);
	
	public E charToInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E charToUInt(CodePosition position, IMethodScope<E> scope, E value);
	
	public E charToString(CodePosition position, IMethodScope<E> scope, E value);
	
	public E stringToChar(CodePosition position, IMethodScope<E> scope, E value);
	
	public E getArrayLength(CodePosition position, IMethodScope<E> scope, E value);
	
	public E getMapLength(CodePosition position, IMethodScope<E> scope, E value);
	
	public E getArrayElement(CodePosition position, IMethodScope<E> scope, E array, E index);
	
	public E setArrayElement(CodePosition position, IMethodScope<E> scope, E array, E index, E value);
	
	public E getRangeFrom(CodePosition position, IMethodScope<E> scope, E value);
	
	public E getRangeTo(CodePosition position, IMethodScope<E> scope, E value);
}
