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
import org.openzen.zencode.AbstractErrorLogger;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.test.expression.TestExpression;
import org.openzen.zencode.util.CodePosition;

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
			ICodeErrorLogger<TestExpression> errorLogger = new AbstractErrorLogger<TestExpression>() {
				@Override
				public void error(CodePosition position, String message)
				{
					super.error(position, message);
				}

				@Override
				public void warning(CodePosition position, String message)
				{
					super.warning(position, message);
				}
			};
			
			ZenLexer lexer = new ZenLexer(errorLogger, type);
			return TypeParser.parse(lexer);
		} catch (IOException ex) {
			throw new AssertionError("Shouldn't happen", ex);
		}
	}
}
