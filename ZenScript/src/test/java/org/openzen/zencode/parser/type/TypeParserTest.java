/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.type;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openzen.zencode.lexer.ZenLexer;

/**
 *
 * @author Stan
 */
public class TypeParserTest
{
	
	public TypeParserTest()
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
	}
	
	@After
	public void tearDown()
	{
	}

	/**
	 * Test of parse method, of class TypeParser.
	 */
	@Test
	public void testParse()
	{
		System.out.println("parse");
		
		assertEquals("byte", getParsedType("byte").toString());
		assertEquals("int[]", getParsedType("int[]").toString());
		assertEquals("int?", getParsedType("int?").toString());
		assertEquals("int[byte]", getParsedType("int[byte]").toString());
		assertEquals("my.pkg.Class", getParsedType("my.pkg.Class").toString());
		assertEquals("my.pkg.Class<>", getParsedType("my.pkg.Class<>").toString());
		assertEquals("my.pkg.Class<int>", getParsedType("my.pkg.Class<int>").toString());
		assertEquals("my.pkg.Class<int,other>", getParsedType("my.pkg.Class<int,other>").toString());
	}
	
	private static IParsedType getParsedType(String type)
	{
		try {
			ZenLexer lexer = new ZenLexer(type);
			return TypeParser.parse(lexer, null);
		} catch (IOException ex) {
			throw new AssertionError("Shouldn't happen", ex);
		}
	}
}
