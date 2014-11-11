/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.util;

import java.util.Arrays;
import java.util.Collections;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stan
 */
public class StringsTest
{
	
	public StringsTest()
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
	 * Test of join method, of class Strings.
	 */
	@Test
	public void testJoin_StringArr_String()
	{
		System.out.println("join");
		
		assertEquals("", Strings.join(new String[0], ","));
		assertEquals("hello", Strings.join(new String[] {"hello"}, ","));
		assertEquals("hello,world", Strings.join(new String[] { "hello", "world" }, ","));
	}

	/**
	 * Test of join method, of class Strings.
	 */
	@Test
	public void testJoin_Iterable_String()
	{
		System.out.println("join");
		
		assertEquals("", Strings.join(Collections.<String>emptyList(), ","));
		assertEquals("hello", Strings.join(Collections.singletonList("hello"), ","));
		assertEquals("hello,world", Strings.join(Arrays.asList("hello", "world"), ","));
	}

	/**
	 * Test of split method, of class Strings.
	 */
	@Test
	public void testSplit()
	{
		System.out.println("split");
		
		String[] exp1 = new String[] { "" };
		String[] exp2 = new String[] { "hello" };
		String[] exp3 = new String[] { "hello", "world" };
		
		assertArrayEquals(exp1, Strings.split("", ','));
		assertArrayEquals(exp2, Strings.split("hello", ','));
		assertArrayEquals(exp3, Strings.split("hello,world", ','));
	}

	/**
	 * Test of splitParagraphs method, of class Strings.
	 */
	@Test
	public void testSplitParagraphs()
	{
		System.out.println("splitParagraphs");
		
		String[] exp1 = new String[] { };
		String[] exp2 = new String[] { "hello world" };
		String[] exp3 = new String[] { "hello", "world" };
		
		assertArrayEquals("", exp1, Strings.splitParagraphs(""));
		assertArrayEquals(exp2, Strings.splitParagraphs("hello\nworld"));
		assertArrayEquals(exp3, Strings.splitParagraphs("hello\n\nworld"));
	}

	/**
	 * Test of unescape method, of class Strings.
	 */
	@Test
	public void testUnescape()
	{
		System.out.println("unescape");
		
		assertEquals("hello", Strings.unescape("\"hello\""));
		assertEquals("hello", Strings.unescape("'hello'"));
		assertEquals("hello\nworld", Strings.unescape("'hello\\nworld'"));
	}
}
