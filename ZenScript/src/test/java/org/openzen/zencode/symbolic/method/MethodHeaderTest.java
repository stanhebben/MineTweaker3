/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openzen.zencode.symbolic.scope.IGlobalScope;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.test.expression.TestExpression;
import org.openzen.zencode.test.type.TestType;
import zenscript.test.TestEnvironment;

/**
 *
 * @author Stan
 */
public class MethodHeaderTest
{
	private IGlobalScope<TestExpression, TestType> scope;
	private TestType typeInt;
	private TestType typeLong;
	private TestType typeString;
	private TestType typeStringArray;
	
	public MethodHeaderTest()
	{
	}
	
	@BeforeClass
	public static void setUpClass()
	{
	}
	
	@AfterClass
	public static void tearDownClass()
	{
	}
	
	@Before
	public void setUp()
	{
		scope = TestEnvironment.createScope();
		typeInt = scope.getTypeCompiler().getInt();
		typeLong = scope.getTypeCompiler().getLong();
		typeString = scope.getTypeCompiler().getString();
		typeStringArray = scope.getTypeCompiler().getArray(typeString);
	}
	
	@After
	public void tearDown()
	{
		scope = null;
	}

	/**
	 * Test of getScope method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetScope()
	{
		System.out.println("getScope");
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		IGlobalScope<TestExpression, TestType> result = instance.getScope();
		assertEquals(scope, result);
	}

	/**
	 * Test of getReturnType method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetReturnType()
	{
		System.out.println("getReturnType");
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		TestType result = instance.getReturnType();
		assertEquals(typeString, result);
	}

	/**
	 * Test of getParameters method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArguments()
	{
		System.out.println("getArguments");
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		List<MethodParameter<TestExpression, TestType>> arguments = instance.getParameters();
		
		assertEquals(typeInt, arguments.get(0).getType());
		assertEquals(typeLong, arguments.get(1).getType());
		assertEquals(typeString, arguments.get(2).getType());
		assertEquals("I", arguments.get(0).getName());
		assertEquals("L", arguments.get(1).getName());
		assertEquals("S", arguments.get(2).getName());
	}

	/**
	 * Test of getParameterByIndex method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArgumentByIndex()
	{
		System.out.println("getArgumentByIndex");
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		
		MethodParameter<TestExpression, TestType> argument = instance.getParameterByIndex(1);
		assertEquals(argument.getType(), typeLong);
	}

	/**
	 * Test of getParameterByName method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArgumentByName()
	{
		System.out.println("getArgumentByName");
		
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		MethodParameter<TestExpression, TestType> result = instance.getParameterByName("S");
		
		assertEquals(typeString, result.getType());
	}

	/**
	 * Test of getParameterIndex method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArgumentIndex()
	{
		System.out.println("getArgumentIndex");
		
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		int result = instance.getParameterIndex("L");
		assertEquals(1, result);
	}

	/**
	 * Test of isVarargs method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testIsVarargs()
	{
		System.out.println("isVarargs");
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		boolean result = instance.isVarargs();
		assertFalse(result);
		
		MethodHeader<TestExpression, TestType> instance2 = construct_ILSV_S();
		boolean result2 = instance2.isVarargs();
		assertTrue(result2);
	}

	/**
	 * Test of accepts method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testAccepts_int()
	{
		System.out.println("accepts");
		
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		assertTrue(instance.accepts(3));
		assertTrue(instance.accepts(2));
		assertFalse(instance.accepts(1));
		
		MethodHeader<TestExpression, TestType> instance2 = construct_ILSV_S();
		assertTrue(instance2.accepts(10));
		assertTrue(instance2.accepts(3));
		assertTrue(instance2.accepts(2));
		assertTrue(instance2.accepts(1));
		assertFalse(instance2.accepts(0));
	}

	/**
	 * Test of getVarArgBaseType method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetVarArgBaseType()
	{
		System.out.println("getVarArgBaseType");
		MethodHeader<TestExpression, TestType> instance = construct_ILSV_S();
		
		TestType result = instance.getVarArgBaseType();
		assertEquals(typeString, result);
	}

	/**
	 * Test of acceptsWithExactTypes method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testAcceptsWithExactTypes()
	{
		System.out.println("acceptsWithExactTypes");
		
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		
		IMethodScope<TestExpression, TestType> staticScope = scope.getConstantEnvironment();
		TestExpression eInt = staticScope.getExpressionCompiler().constantInt(null, staticScope, 0);
		TestExpression eLong = staticScope.getExpressionCompiler().constantLong(null, staticScope, 1);
		TestExpression eString = staticScope.getExpressionCompiler().constantString(null, staticScope, "Hello");
		
		assertTrue(instance.acceptsWithExactTypes(eInt, eLong, eString));
		assertFalse(instance.acceptsWithExactTypes(eInt, eInt, eString));
		
		MethodHeader<TestExpression, TestType> instance2 = construct_ILSV_S();
		assertTrue(instance2.acceptsWithExactTypes(eInt));
		assertTrue(instance2.acceptsWithExactTypes(eInt, eLong));
		assertTrue(instance2.acceptsWithExactTypes(eInt, eLong, eString));
		assertTrue(instance2.acceptsWithExactTypes(eInt, eLong, eString, eString));
		
		assertFalse(instance2.acceptsWithExactTypes(eInt, eInt));
		assertFalse(instance2.acceptsWithExactTypes(eInt, eLong, eInt));
	}

	/**
	 * Test of accepts method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testAccepts_ExpressionArr()
	{
		System.out.println("accepts");
		
		MethodHeader<TestExpression, TestType> instance = construct_ILS_S();
		
		IMethodScope<TestExpression, TestType> staticScope = scope.getConstantEnvironment();
		TestExpression eInt = staticScope.getExpressionCompiler().constantInt(null, staticScope, 0);
		TestExpression eLong = staticScope.getExpressionCompiler().constantLong(null, staticScope, 1);
		TestExpression eString = staticScope.getExpressionCompiler().constantString(null, staticScope, "Hello");
		TestExpression eBool = staticScope.getExpressionCompiler().constantBool(null, staticScope, false);
		
		assertTrue(instance.accepts(eInt, eLong, eString));
		assertTrue(instance.accepts(eInt, eInt, eString));
		assertTrue(instance.accepts(eInt, eString, eString));
		assertTrue(instance.accepts(eInt, eLong));
		assertFalse(instance.accepts(eInt, eBool));
		
		MethodHeader<TestExpression, TestType> instance2 = construct_ILSV_S();
		
		assertTrue(instance2.accepts(eInt, eLong, eString));
		assertTrue(instance2.accepts(eInt, eInt, eString));
		assertTrue(instance2.accepts(eInt, eLong));
		assertTrue(instance2.accepts(eInt));
		assertTrue(instance2.accepts(eLong));
		assertTrue(instance2.accepts(eInt, eString, eLong));
		assertFalse(instance2.accepts(eInt, eBool));
	}

	/**
	 * Test of getArgumentType method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArgumentType()
	{
		System.out.println("getArgumentType");
		
		MethodHeader<TestExpression, TestType> instance = construct_ILSV_S();
		
		assertEquals(typeLong, instance.getArgumentType(1));
		assertEquals(typeStringArray, instance.getArgumentType(2));
	}
	
	private MethodHeader<TestExpression, TestType> construct_ILS_S()
	{
		List<MethodParameter<TestExpression, TestType>> arguments = new ArrayList<MethodParameter<TestExpression, TestType>>();
		arguments.add(new MethodParameter<TestExpression, TestType>("I", typeInt, null));
		arguments.add(new MethodParameter<TestExpression, TestType>("L", typeLong, null));
		
		TestExpression defaultStringValue = scope.getExpressionCompiler().constantString(null, scope.getConstantEnvironment(), "Hello");
		arguments.add(new MethodParameter<TestExpression, TestType>("S", typeString, defaultStringValue));
		
		return new MethodHeader<TestExpression, TestType>(typeString, arguments, false);
	}
	
	private MethodHeader<TestExpression, TestType> construct_ILSV_S()
	{
		List<MethodParameter<TestExpression, TestType>> arguments = new ArrayList<MethodParameter<TestExpression, TestType>>();
		arguments.add(new MethodParameter<TestExpression, TestType>("I", typeInt, null));
		
		TestExpression defaultLongValue = scope.getExpressionCompiler().constantLong(null, scope.getConstantEnvironment(), 1);
		arguments.add(new MethodParameter<TestExpression, TestType>("L", typeLong, defaultLongValue));
		arguments.add(new MethodParameter<TestExpression, TestType>("S", typeStringArray, null));
		
		return new MethodHeader<TestExpression, TestType>(typeString, arguments, true);
	}
}
