/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.expression.JavaAndAnd;
import org.openzen.zencode.java.expression.JavaArrayLiteral;
import org.openzen.zencode.java.expression.JavaBool;
import org.openzen.zencode.java.expression.JavaByte;
import org.openzen.zencode.java.expression.JavaChar;
import org.openzen.zencode.java.expression.JavaCompareGeneric;
import org.openzen.zencode.java.expression.JavaDouble;
import org.openzen.zencode.java.expression.JavaFloat;
import org.openzen.zencode.java.expression.JavaInt;
import org.openzen.zencode.java.expression.JavaInvalid;
import org.openzen.zencode.java.expression.JavaLocalGet;
import org.openzen.zencode.java.expression.JavaLocalSet;
import org.openzen.zencode.java.expression.JavaLong;
import org.openzen.zencode.java.expression.JavaNotNull;
import org.openzen.zencode.java.expression.JavaNull;
import org.openzen.zencode.java.expression.JavaOrOr;
import org.openzen.zencode.java.expression.JavaRange;
import org.openzen.zencode.java.expression.JavaShort;
import org.openzen.zencode.java.expression.JavaString;
import org.openzen.zencode.java.expression.JavaTernary;
import org.openzen.zencode.java.expression.JavaUByte;
import org.openzen.zencode.java.expression.JavaUInt;
import org.openzen.zencode.java.expression.JavaULong;
import org.openzen.zencode.java.expression.JavaUShort;
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
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.util.CodePosition;

/**
 * Constructs a java expression compiler.
 * 
 * @author Stan
 */
public class JavaExpressionCompiler implements IExpressionCompiler<IJavaExpression>
{
	private final JavaCompileState compileState;
	
	public JavaExpressionCompiler(JavaCompileState compileState)
	{
		this.compileState = compileState;
	}

	@Override
	public IJavaExpression invalid(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		return invalid(position, scope, scope.getTypeCompiler().any);
	}

	@Override
	public IJavaExpression invalid(CodePosition position, IMethodScope<IJavaExpression> scope, IGenericType<IJavaExpression> valueType)
	{
		return new JavaInvalid(position, scope, valueType);
	}

	@Override
	public IJavaExpression constantNull(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		return new JavaNull(position, scope);
	}

	@Override
	public IJavaExpression constantBool(CodePosition position, IMethodScope<IJavaExpression> scope, boolean value)
	{
		return new JavaBool(position, scope, value);
	}

	@Override
	public IJavaExpression constantByte(CodePosition position, IMethodScope<IJavaExpression> scope, byte value)
	{
		return new JavaByte(position, scope, value);
	}

	@Override
	public IJavaExpression constantUByte(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		return new JavaUByte(position, scope, value);
	}

	@Override
	public IJavaExpression constantShort(CodePosition position, IMethodScope<IJavaExpression> scope, short value)
	{
		return new JavaShort(position, scope, value);
	}

	@Override
	public IJavaExpression constantUShort(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		return new JavaUShort(position, scope, value);
	}

	@Override
	public IJavaExpression constantInt(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		return new JavaInt(position, scope, value);
	}

	@Override
	public IJavaExpression constantUInt(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		return new JavaUInt(position, scope, value);
	}

	@Override
	public IJavaExpression constantLong(CodePosition position, IMethodScope<IJavaExpression> scope, long value)
	{
		return new JavaLong(position, scope, value);
	}

	@Override
	public IJavaExpression constantULong(CodePosition position, IMethodScope<IJavaExpression> scope, long value)
	{
		return new JavaULong(position, scope, value);
	}

	@Override
	public IJavaExpression constantFloat(CodePosition position, IMethodScope<IJavaExpression> scope, float value)
	{
		return new JavaFloat(position, scope, value);
	}

	@Override
	public IJavaExpression constantDouble(CodePosition position, IMethodScope<IJavaExpression> scope, double value)
	{
		return new JavaDouble(position, scope, value);
	}

	@Override
	public IJavaExpression constantChar(CodePosition position, IMethodScope<IJavaExpression> scope, int value)
	{
		return new JavaChar(position, scope, value);
	}

	@Override
	public IJavaExpression constantString(CodePosition position, IMethodScope<IJavaExpression> scope, String value)
	{
		return new JavaString(position, scope, value);
	}

	@Override
	public IJavaExpression constant(CodePosition position, IMethodScope<IJavaExpression> scope, Object value)
	{
		if (value.getClass() == Boolean.class)
			return constantBool(position, scope, (Boolean) value);
		else if (value.getClass() == Byte.class)
			return constantByte(position, scope, (Byte) value);
		else if (value.getClass() == Short.class)
			return constantShort(position, scope, (Short) value);
		else if (value.getClass() == Integer.class)
			return constantInt(position, scope, (Integer) value);
		else if (value.getClass() == Long.class)
			return constantLong(position, scope, (Long) value);
		else if (value.getClass() == Float.class)
			return constantFloat(position, scope, (Float) value);
		else if (value.getClass() == Double.class)
			return constantDouble(position, scope, (Double) value);
		else if (value.getClass() == Character.class)
			return constantChar(position, scope, (Character) value);
		else if (value.getClass() == String.class)
			return constantString(position, scope, (String) value);
		else
			throw new IllegalArgumentException("Invalid constant class: " + value);
	}

	@Override
	public List<IJavaExpression> constants(CodePosition position, IMethodScope<IJavaExpression> scope, Object[] values)
	{
		List<IJavaExpression> results = new ArrayList<>();
		for (Object value : values) {
			results.add(constant(position, scope, value));
		}
		return results;
	}

	@Override
	public IJavaExpression localGet(CodePosition position, IMethodScope<IJavaExpression> scope, LocalSymbol<IJavaExpression> local)
	{
		return new JavaLocalGet(position, scope, local);
	}

	@Override
	public IJavaExpression localSet(CodePosition position, IMethodScope<IJavaExpression> scope, LocalSymbol<IJavaExpression> local, IJavaExpression value)
	{
		return new JavaLocalSet(position, scope, local, value);
	}

	@Override
	public IJavaExpression andAnd(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		return new JavaAndAnd(position, scope, left, right);
	}

	@Override
	public IJavaExpression orOr(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		return new JavaOrOr(position, scope, left, right);
	}

	@Override
	public IJavaExpression compareGeneric(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value, CompareType comparator)
	{
		return new JavaCompareGeneric(position, scope, value, comparator);
	}

	@Override
	public IJavaExpression ternary(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression condition, IJavaExpression ifValue, IJavaExpression elseValue)
	{
		return new JavaTernary(position, scope, condition, ifValue, elseValue);
	}

	@Override
	public IJavaExpression notNull(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		return new JavaNotNull(position, scope, value);
	}

	@Override
	public IJavaExpression range(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression from, IJavaExpression to)
	{
		return new JavaRange(position, scope, from, to);
	}

	@Override
	public IJavaExpression array(CodePosition position, IMethodScope<IJavaExpression> scope, IGenericType<IJavaExpression> arrayType, List<IJavaExpression> values)
	{
		return new JavaArrayLiteral(position, scope, arrayType, values);
	}

	@Override
	public IJavaExpression map(CodePosition position, IMethodScope<IJavaExpression> scope, IGenericType<IJavaExpression> mapType, List<IJavaExpression> keys, List<IJavaExpression> values)
	{
		return new JavaMapLiteral(position, scope, mapType, keys, values);
	}

	@Override
	public IJavaExpression constructNew(CodePosition position, IMethodScope<IJavaExpression> scope, TypeInstance<IJavaExpression> type, ConstructorMember<IJavaExpression> constructor, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression constructNewGeneric(CodePosition position, IMethodScope<IJavaExpression> scope, TypeInstance<IJavaExpression> type, ITypeVariable<IJavaExpression> typeVariable, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression functionExpression(CodePosition position, IMethodScope<IJavaExpression> scope, MethodHeader<IJavaExpression> header, List<Statement<IJavaExpression>> statements)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression staticMethodValue(CodePosition position, IMethodScope<IJavaExpression> scope, IMethod<IJavaExpression> method)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyNot(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyNeg(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyInvert(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyAdd(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anySub(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyCat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyMul(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyDiv(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyMod(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression anyCastTo(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value, TypeInstance<IJavaExpression> type)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression virtualGetter(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression instance, IGetterMember<IJavaExpression> getter)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression virtualSetter(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression instance, ISetterMember<IJavaExpression> setter, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression staticGetter(CodePosition position, IMethodScope<IJavaExpression> scope, IGetterMember<IJavaExpression> getter)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression staticSetter(CodePosition position, IMethodScope<IJavaExpression> scope, ISetterMember<IJavaExpression> setter, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callOperator(CodePosition position, IMethodScope<IJavaExpression> scope, IOperatorMember<IJavaExpression> member, IJavaExpression instance, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callVirtualCaller(CodePosition position, IMethodScope<IJavaExpression> scope, ICallerMember<IJavaExpression> member, IJavaExpression instance, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callStaticCaller(CodePosition position, IMethodScope<IJavaExpression> scope, ICallerMember<IJavaExpression> member, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callVirtualMethod(CodePosition position, IMethodScope<IJavaExpression> scope, IMethodMember<IJavaExpression> member, IJavaExpression instance, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callStaticMethod(CodePosition position, IMethodScope<IJavaExpression> scope, IMethodMember<IJavaExpression> member, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callFunction(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression notBool(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andBool(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression a, IJavaExpression b)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orBool(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression a, IJavaExpression b)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorBool(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression a, IJavaExpression b)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression equalsBool(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression a, IJavaExpression b)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression notEqualsBool(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression a, IJavaExpression b)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression negByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invertByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shlByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shrByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareByte(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invertUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shlUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shrUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushrUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareUByte(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invertShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression negShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shlShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shrShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushrShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareShort(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invertUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shlUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shrUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareUShort(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression negInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invertInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addInt(CodePosition positino, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shlInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shrInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushrInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareInt(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invertUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shlUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shrUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushrUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareUInt(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression negLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invertLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shlLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shrLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushrLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareLong(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression invertULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression andULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression orULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression xorULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shlULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shrULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushrULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareULong(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression negFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareFloat(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression negDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression addDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression subDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression mulDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression divDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression modDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareDouble(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareChar(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringBool(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringChar(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression catStringString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression indexString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value, IJavaExpression index)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression compareString(CodePosition position, IMethodScope<IJavaExpression> scope, CompareType compareType, IJavaExpression left, IJavaExpression right)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression boolToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression byteToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ubyteToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression shortToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ushortToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression intToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression uintToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression longToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression ulongToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToDouble(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression floatToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToLong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToULong(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToFloat(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression doubleToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression charToByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression charToUByte(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression charToShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression charToUShort(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression charToInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression charToUInt(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression charToString(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression stringToChar(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression getArrayLength(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression getMapLength(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression getArrayElement(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression array, IJavaExpression index)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression setArrayElement(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression array, IJavaExpression index, IJavaExpression value)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
