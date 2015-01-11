/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openzen.zencode.parser.ParsedModule;
import org.openzen.zencode.java.JavaCompiler;

/**
 *
 * @author Stan
 */
public class BasicCodeTest
{
	private TestEnvironment environment;
	
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
		environment = new TestEnvironment();
	}
	
	@After
	public void tearDown()
	{
		environment = null;
	}

	/**
	 * Test of getScope method, of class MethodHeader.
	 */
	@org.junit.Test
	public void testPrint()
	{
		System.out.println("print");
		JavaCompiler compiler = new JavaCompiler(environment);
		
		ParsedModule module = compiler.createAndAddModule("test", null);
		module.addScript("test.zs", "print(\"Hello World!\");");
		
		compiler.compile().run();
		
		environment.consumePrint("Hello World!");
		environment.noMoreMessages();
	}
}
