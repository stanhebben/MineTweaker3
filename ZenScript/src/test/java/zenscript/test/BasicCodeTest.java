/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.test;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openzen.zencode.parser.ParsedModule;
import org.openzen.zencode.parser.ParserEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;

/**
 *
 * @author Stan
 */
public class BasicCodeTest
{
	private IScopeGlobal scope;
	
	public BasicCodeTest()
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
	public void testPrint()
	{
		System.out.println("print");
		ParserEnvironment environment = new ParserEnvironment(scope);
		
		ParsedModule module = new ParsedModule(environment, null, "test");
		module.addScript("test.zs", "print(\"Hello World!\");");
		environment.addModule(module);
		
		environment.compile().run();
		
		TestEnvironment.INSTANCE.consumePrint("Hello World!");
		TestEnvironment.INSTANCE.noMoreMessages();
	}
}
