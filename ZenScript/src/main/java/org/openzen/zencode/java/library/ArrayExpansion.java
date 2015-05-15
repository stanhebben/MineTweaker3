/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openzen.zencode.annotations.ZenExpansion;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMethod;

/**
 *
 * @author Stan
 */
@ZenExpansion("<T>[]")
public class ArrayExpansion
{
	@ZenGetter("first")
	public static <T> T getFirst(T[] array)
	{
		return array.length == 0 ? null : array[0];
	}
	
	@ZenGetter("first")
	public static <T> T getFirst(List<T> array)
	{
		return array.isEmpty() ? null : array.get(0);
	}
	
	@ZenGetter("last")
	public static <T> T getLast(T[] array)
	{
		return array.length == 0 ? null : array[array.length - 1];
	}
	
	@ZenGetter("last")
	public static <T> T getLast(List<T> array)
	{
		return array.isEmpty() ? null : array.get(array.size() - 1);
	}
	
	@ZenMethod
	public static <T> void reverse(T[] array)
	{
		for (int i = 0; i < array.length / 2; i++) {
			T tmp = array[i];
			array[i] = array[array.length - i - 1];
			array[array.length - i - 1] = tmp;
		}
	}
	
	@ZenMethod
	public static <T> void reverse(List<T> array)
	{
		for (int i = 0; i < array.size() / 2; i++) {
			T tmp = array.get(i);
			array.set(i, array.get(array.size() - i - 1));
			array.set(array.size() - i - 1, tmp);
		}
	}
	
	@ZenMethod
	public static <T> void sort(T[] array, Comparator<T> comparator)
	{
		Arrays.sort(array, comparator);
	}
	
	@ZenMethod
	public static <T> void sort(List<T> array, Comparator<T> comparator)
	{
		Collections.sort(array, comparator);
	}
	
	@ZenGetter
	public static <T> T[] reversed(T[] array)
	{
		T[] copy = Arrays.copyOf(array, array.length);
		reverse(copy);
		return copy;
	}
	
	@ZenGetter
	public static <T> List<T> reversed(List<T> array)
	{
		List<T> copy = new ArrayList<>(array);
		reverse(copy);
		return copy;
	}
	
	@ZenMethod
	public static <T> T[] sorted(T[] array, Comparator<T> comparator)
	{
		T[] copy = Arrays.copyOf(array, array.length);
		Arrays.sort(copy);
		return copy;
	}
	
	@ZenMethod
	public static <T> List<T> sorted(List<T> array, Comparator<T> comparator)
	{
		List<T> copy = new ArrayList<>(array);
		Collections.sort(array, comparator);
		return copy;
	}
}
