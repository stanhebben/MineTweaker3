/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.runtime;

import java.util.Iterator;

/**
 * IAny is the runtime class of the any type. It provides all necessary functions
 * to implement the specified behavior of the any type and completely reflect the
 * operations that can be performed on the underlying type.
 * 
 * @author Stan Hebben
 */
public interface IAny {
	public static final int NUM_BYTE = 1;
	public static final int NUM_SHORT = 2;
	public static final int NUM_INT = 3;
	public static final int NUM_LONG = 4;
	public static final int NUM_FLOAT = 5;
	public static final int NUM_DOUBLE = 6;
	
	public static final String NAME = IAny.class.getName();
	public static final String SIGNATURE = "L" + NAME + ";";
	
	/**
	 * Performs the not (!) operation. Only valid on boolean values. Should
	 * throw an IllegalOperationException if the underlying type does not support
	 * that operation.
	 * 
	 * @return not result
	 */
	public IAny not();
	
	/**
	 * Performs the logical inverse (~) operation. Should throw an
	 * IllegalOperationException if the underlying type does not support that
	 * operation.
	 * 
	 * @return inversion result
	 */
	public IAny invert();
	
	/**
	 * Performs the negate (-) operation. Should throw an IllegalArgumentException
	 * if the underlying type doesn't support that operation.
	 * 
	 * @return negate result
	 */
	public IAny neg();
	
	public IAny add(IAny value);
	
	public IAny sub(IAny value);
	
	public IAny cat(IAny value);
	
	public IAny mul(IAny value);
	
	public IAny div(IAny value);
	
	public IAny mod(IAny value);
	
	public IAny and(IAny value);
	
	public IAny or(IAny value);
	
	public IAny xor(IAny value);
	
	public IAny range(IAny value);
	
	public int compareTo(IAny value);
	
	public boolean contains(IAny value);
	
	public IAny memberGet(String member);
	
	public void memberSet(String member, IAny value);
	
	public IAny memberCall(String member, IAny... values);
	
	public IAny indexGet(IAny key);
	
	public void indexSet(IAny key, IAny value);
	
	public IAny call(IAny... values);
	
	public boolean asBool();
	
	public byte asByte();
	
	public short asShort();
	
	public int asInt();
	
	public long asLong();
	
	public float asFloat();
	
	public double asDouble();
	
	public String asString();
	
	/**
	 * Attempts to convert this value to the given type. Should return null if
	 * the conversion is not possible.
	 * 
	 * @param <T> class to convert to
	 * @param cls class to convert to
	 * @return converted object
	 */
	public <T> T as(Class<T> cls);
	
	/**
	 * Checks if this value is of the given type. Implements the is operator.
	 * Should return true for this class, its superclasses, and its implemented
	 * interfaces.
	 * 
	 * Casters are irrelevant to this check.
	 * 
	 * @param cls class to check
	 * @return true if this value is of given type
	 */
	public boolean is(Class<?> cls);
	
	/**
	 * Checks if this value can be implicitly cast to the given type. Should
	 * return true on all values for with is(cls) is true, for all types for
	 * which a caster is available, and for all subtypes for which a caster
	 * is available.
	 * 
	 * @param cls
	 * @return 
	 */
	public boolean canCastImplicit(Class<?> cls);
	
	/**
	 * Returns the number type. Should return NONE if this is not a number.
	 * 
	 * @return number type
	 */
	public NumberType getNumberType();
	
	public Iterator<IAny> iteratorSingle();
	
	public Iterator<IAny[]> iteratorMulti(int n);
}
