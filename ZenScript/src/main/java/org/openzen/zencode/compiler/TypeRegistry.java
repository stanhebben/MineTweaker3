/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.compiler;

import java.util.Arrays;
import java.util.Collections;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ArrayTypeDefinition;
import org.openzen.zencode.symbolic.type.FunctionTypeDefinition;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.ITypeDefinition;
import org.openzen.zencode.symbolic.type.MapTypeDefinition;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.basic.AnyTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.BoolTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.ByteTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.CharTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.DoubleTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.FloatTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.IntTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.LongTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.NullTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.RangeTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.ShortTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.StringTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.UByteTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.UIntTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.ULongTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.UShortTypeDefinition;
import org.openzen.zencode.symbolic.type.basic.VoidTypeDefinition;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;

/**
 *
 * @author Stan
 * @param <E>
 */
public class TypeRegistry<E extends IPartialExpression<E>>
{
	public final BoolTypeDefinition<E> boolDefinition;
	public final ByteTypeDefinition<E> byteDefinition;
	public final UByteTypeDefinition<E> ubyteDefinition;
	public final ShortTypeDefinition<E> shortDefinition;
	public final UShortTypeDefinition<E> ushortDefinition;
	public final IntTypeDefinition<E> intDefinition;
	public final UIntTypeDefinition<E> uintDefinition;
	public final LongTypeDefinition<E> longDefinition;
	public final ULongTypeDefinition<E> ulongDefinition;
	public final FloatTypeDefinition<E> floatDefinition;
	public final DoubleTypeDefinition<E> doubleDefinition;
	public final CharTypeDefinition<E> charDefinition;
	public final StringTypeDefinition<E> stringDefinition;
	public final RangeTypeDefinition<E> rangeDefinition;
	public final AnyTypeDefinition<E> anyDefinition;
	public final VoidTypeDefinition<E> voidDefinition;
	public final NullTypeDefinition<E> nullDefinition;
	
	public final ArrayTypeDefinition<E> arrayDefinition;
	public final MapTypeDefinition<E> mapDefinition;
	
	public final IGenericType<E> bool;
	public final IGenericType<E> byte_;
	public final IGenericType<E> ubyte;
	public final IGenericType<E> short_;
	public final IGenericType<E> ushort;
	public final IGenericType<E> int_;
	public final IGenericType<E> uint;
	public final IGenericType<E> long_;
	public final IGenericType<E> ulong;
	public final IGenericType<E> float_;
	public final IGenericType<E> double_;
	public final IGenericType<E> char_;
	public final IGenericType<E> string;
	public final IGenericType<E> range;
	public final IGenericType<E> any;
	public final IGenericType<E> void_;
	public final IGenericType<E> null_;
	
	public final IGenericType<E> anyArray;
	public final IGenericType<E> anyMap;
	
	public TypeRegistry()
	{
		boolDefinition = new BoolTypeDefinition<>();
		byteDefinition = new ByteTypeDefinition<>();
		ubyteDefinition = new UByteTypeDefinition<>();
		shortDefinition = new ShortTypeDefinition<>();
		ushortDefinition = new UShortTypeDefinition<>();
		intDefinition = new IntTypeDefinition<>();
		uintDefinition = new UIntTypeDefinition<>();
		longDefinition = new LongTypeDefinition<>();
		ulongDefinition = new ULongTypeDefinition<>();
		floatDefinition = new FloatTypeDefinition<>();
		doubleDefinition = new DoubleTypeDefinition<>();
		charDefinition = new CharTypeDefinition<>();
		stringDefinition = new StringTypeDefinition<>();
		rangeDefinition = new RangeTypeDefinition<>();
		anyDefinition = new AnyTypeDefinition<>();
		voidDefinition = new VoidTypeDefinition<>();
		nullDefinition = new NullTypeDefinition<>();
		
		arrayDefinition = new ArrayTypeDefinition<>();
		mapDefinition = new MapTypeDefinition<>();
		
		bool = new TypeInstance<>(boolDefinition);
		byte_ = new TypeInstance<>(byteDefinition);
		ubyte = new TypeInstance<>(ubyteDefinition);
		short_ = new TypeInstance<>(shortDefinition);
		ushort = new TypeInstance<>(ushortDefinition);
		int_ = new TypeInstance<>(intDefinition);
		uint = new TypeInstance<>(uintDefinition);
		long_ = new TypeInstance<>(longDefinition);
		ulong = new TypeInstance<>(ulongDefinition);
		float_ = new TypeInstance<>(floatDefinition);
		double_ = new TypeInstance<>(doubleDefinition);
		char_ = new TypeInstance<>(charDefinition);
		string = new TypeInstance<>(stringDefinition);
		range = new TypeInstance<>(rangeDefinition);
		any = new TypeInstance<>(anyDefinition);
		void_ = new TypeInstance<>(voidDefinition);
		null_ = new TypeInstance<>(nullDefinition);
		
		anyArray = new TypeInstance<>(arrayDefinition, Collections.singletonList(any), false);
		anyMap = new TypeInstance<>(mapDefinition, Arrays.asList(any, any), false);
	}
	
	public void init(IModuleScope<E> systemScope)
	{
		boolDefinition.init(this, systemScope);
		byteDefinition.init(this, systemScope);
		ubyteDefinition.init(this, systemScope);
		shortDefinition.init(this, systemScope);
		ushortDefinition.init(this, systemScope);
		intDefinition.init(this, systemScope);
		uintDefinition.init(this, systemScope);
		longDefinition.init(this, systemScope);
		ulongDefinition.init(this, systemScope);
		floatDefinition.init(this, systemScope);
		doubleDefinition.init(this, systemScope);
		charDefinition.init(this, systemScope);
		stringDefinition.init(this, systemScope);
		rangeDefinition.init(this, systemScope);
		anyDefinition.init(this, systemScope);
	}
	
	public IGenericType<E> getArray(IGenericType<E> baseType)
	{
		return new TypeInstance<>(arrayDefinition, Arrays.asList(baseType), false);
	}
	
	public IGenericType<E> getMap(IGenericType<E> keyType, IGenericType<E> valueType)
	{
		return new TypeInstance<>(mapDefinition, Arrays.asList(keyType, valueType), false);
	}
	
	public IGenericType<E> getFunction(MethodHeader<E> methodHeader)
	{
		return getInstance(new FunctionTypeDefinition<>(methodHeader));
	}
	
	private IGenericType<E> getInstance(ITypeDefinition<E> type)
	{
		return new TypeInstance<>(type, TypeCapture.empty(), false);
	}
}
