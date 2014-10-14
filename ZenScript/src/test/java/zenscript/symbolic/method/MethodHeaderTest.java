/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.method;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import zenscript.test.TestEnvironment;

/**
 *
 * @author Stan
 */
public class MethodHeaderTest
{
	private IScopeGlobal scope;
	private ZenType typeInt;
	private ZenType typeLong;
	private ZenType typeString;
	private ZenType typeStringArray;
	
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
		typeInt = scope.getTypes().INT;
		typeLong = scope.getTypes().LONG;
		typeString = scope.getTypes().STRING;
		typeStringArray = new ZenTypeArrayBasic(typeString);
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
		MethodHeader instance = construct_ILS_S();
		IScopeGlobal result = instance.getScope();
		assertEquals(scope, result);
	}

	/**
	 * Test of getReturnType method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetReturnType()
	{
		System.out.println("getReturnType");
		MethodHeader instance = construct_ILS_S();
		ZenType result = instance.getReturnType();
		assertEquals(typeString, result);
	}

	/**
	 * Test of getArguments method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArguments()
	{
		System.out.println("getArguments");
		MethodHeader instance = construct_ILS_S();
		List<MethodArgument> arguments = instance.getArguments();
		
		assertEquals(typeInt, arguments.get(0).getType());
		assertEquals(typeLong, arguments.get(1).getType());
		assertEquals(typeString, arguments.get(2).getType());
		assertEquals("I", arguments.get(0).getName());
		assertEquals("L", arguments.get(1).getName());
		assertEquals("S", arguments.get(2).getName());
	}

	/**
	 * Test of getArgumentByIndex method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArgumentByIndex()
	{
		System.out.println("getArgumentByIndex");
		MethodHeader instance = construct_ILS_S();
		
		MethodArgument argument = instance.getArgumentByIndex(1);
		assertEquals(argument.getType(), typeLong);
	}

	/**
	 * Test of getArgumentByName method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArgumentByName()
	{
		System.out.println("getArgumentByName");
		
		MethodHeader instance = construct_ILS_S();
		MethodArgument result = instance.getArgumentByName("S");
		
		assertEquals(typeString, result.getType());
	}

	/**
	 * Test of getArgumentIndex method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArgumentIndex()
	{
		System.out.println("getArgumentIndex");
		
		MethodHeader instance = construct_ILS_S();
		int result = instance.getArgumentIndex("L");
		assertEquals(1, result);
	}

	/**
	 * Test of isVarargs method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testIsVarargs()
	{
		System.out.println("isVarargs");
		MethodHeader instance = construct_ILS_S();
		boolean result = instance.isVarargs();
		assertFalse(result);
		
		MethodHeader instance2 = construct_ILSV_S();
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
		
		MethodHeader instance = construct_ILS_S();
		assertTrue(instance.accepts(3));
		assertTrue(instance.accepts(2));
		assertFalse(instance.accepts(1));
		
		MethodHeader instance2 = construct_ILSV_S();
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
		MethodHeader instance = construct_ILSV_S();
		
		ZenType result = instance.getVarArgBaseType();
		assertEquals(typeString, result);
	}

	/**
	 * Test of acceptsWithExactTypes method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testAcceptsWithExactTypes()
	{
		System.out.println("acceptsWithExactTypes");
		
		MethodHeader instance = construct_ILS_S();
		
		IScopeMethod staticScope = scope.getTypes().getStaticGlobalEnvironment();
		Expression eInt = new ExpressionInt(null, staticScope, 0, typeInt);
		Expression eLong = new ExpressionInt(null, staticScope, 1, typeLong);
		Expression eString = new ExpressionString(null, staticScope, "Hello");
		
		assertTrue(instance.acceptsWithExactTypes(eInt, eLong, eString));
		assertFalse(instance.acceptsWithExactTypes(eInt, eInt, eString));
		
		MethodHeader instance2 = construct_ILSV_S();
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
		
		MethodHeader instance = construct_ILS_S();
		
		IScopeMethod staticScope = scope.getTypes().getStaticGlobalEnvironment();
		Expression eInt = new ExpressionInt(null, staticScope, 0, typeInt);
		Expression eLong = new ExpressionInt(null, staticScope, 1, typeLong);
		Expression eString = new ExpressionString(null, staticScope, "Hello");
		
		assertTrue(instance.accepts(eInt, eLong, eString));
		assertTrue(instance.accepts(eInt, eInt, eString));
		assertFalse(instance.accepts(eInt, eString, eString));
		assertFalse(instance.accepts(eInt, eLong));
		
		MethodHeader instance2 = construct_ILSV_S();
		
		assertTrue(instance2.accepts(eInt, eLong, eString));
		assertTrue(instance2.accepts(eInt, eInt, eString));
		assertTrue(instance2.accepts(eInt, eLong));
		assertTrue(instance2.accepts(eInt));
		assertTrue(instance2.accepts(eLong));
		assertFalse(instance2.accepts(eInt, eString, eLong));
	}

	/**
	 * Test of getArgumentType method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testGetArgumentType()
	{
		System.out.println("getArgumentType");
		
		MethodHeader instance = construct_ILSV_S();
		
		assertEquals(typeLong, instance.getArgumentType(1));
		assertEquals(typeStringArray, instance.getArgumentType(2));
	}
	
	private MethodHeader construct_ILS_S()
	{
		List<MethodArgument> arguments = new ArrayList<MethodArgument>();
		arguments.add(new MethodArgument("I", typeInt, null));
		arguments.add(new MethodArgument("L", typeLong, null));
		
		Expression defaultStringValue = new ExpressionString(null, scope.getTypes().getStaticGlobalEnvironment(), "Hello");
		arguments.add(new MethodArgument("S", typeString, defaultStringValue));
		
		return new MethodHeader(typeString, arguments, false);
	}
	
	private MethodHeader construct_ILSV_S()
	{
		List<MethodArgument> arguments = new ArrayList<MethodArgument>();
		arguments.add(new MethodArgument("I", typeInt, null));
		
		Expression defaultLongValue = new ExpressionInt(null, scope.getTypes().getStaticGlobalEnvironment(), 1, typeLong);
		arguments.add(new MethodArgument("L", typeLong, defaultLongValue));
		arguments.add(new MethodArgument("S", typeStringArray, null));
		
		return new MethodHeader(typeString, arguments, true);
	}
}
