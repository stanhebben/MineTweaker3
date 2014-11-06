/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.runtime;

import java.util.Iterator;

/**
 *
 * @author Stan
 */
public class AnyBool implements IAny {
	public static final AnyBool TRUE = new AnyBool(true);
	public static final AnyBool FALSE = new AnyBool(false);
	
	public static AnyBool valueOf(boolean value) {
		return value ? TRUE: FALSE;
	}
	
	private final boolean value;
	
	private AnyBool(boolean value) {
		this.value = value;
	}
	
	@Override
	public IAny not() {
		return value ? FALSE : TRUE;
	}
	
	@Override
	public IAny invert() {
		throw new UnsupportedOperationException("bool doesn't support the ~ operator");
	}

	@Override
	public IAny neg() {
		throw new UnsupportedOperationException("bool doesn't support arithmetic operators");
	}

	@Override
	public IAny add(IAny value) {
		throw new UnsupportedOperationException("bool doesn't support arithmetic operators");
	}

	@Override
	public IAny sub(IAny value) {
		throw new UnsupportedOperationException("bool doesn't support arithmetic operators");
	}

	@Override
	public IAny cat(IAny value) {
		throw new UnsupportedOperationException("bool doesn't support arithmetic operators");
	}

	@Override
	public IAny mul(IAny value) {
		throw new UnsupportedOperationException("bool doesn't support arithmetic operators");
	}

	@Override
	public IAny div(IAny value) {
		throw new UnsupportedOperationException("bool doesn't support arithmetic operators");
	}

	@Override
	public IAny mod(IAny value) {
		throw new UnsupportedOperationException("bool doesn't support arithmetic operators");
	}

	@Override
	public IAny and(IAny value) {
		return valueOf(this.value & value.asBool());
	}

	@Override
	public IAny or(IAny value) {
		return valueOf(this.value | value.asBool());
	}

	@Override
	public IAny xor(IAny value) {
		return valueOf(this.value ^ value.asBool());
	}

	@Override
	public IAny range(IAny value) {
		throw new UnsupportedOperationException("bool doesn't support ranges");
	}

	@Override
	public int compareTo(IAny value) {
		throw new UnsupportedOperationException("bool doesn't support comparison");
	}

	@Override
	public boolean contains(IAny value) {
		throw new UnsupportedOperationException("bool cannot contain other values");
	}

	@Override
	public IAny memberGet(String member) {
		throw new UnsupportedOperationException("bool doesn't have members");
	}

	@Override
	public void memberSet(String member, IAny value) {
		throw new UnsupportedOperationException("bool doesn't have members");
	}

	@Override
	public IAny memberCall(String member, IAny... values) {
		throw new UnsupportedOperationException("bool doesn't have members");
	}

	@Override
	public IAny indexGet(IAny key) {
		throw new UnsupportedOperationException("bool doesn't support indexing");
	}

	@Override
	public void indexSet(IAny key, IAny value) {
		throw new UnsupportedOperationException("bool doesn't support indexing");
	}

	@Override
	public IAny call(IAny... values) {
		throw new UnsupportedOperationException("Cannot call booleans");
	}

	@Override
	public boolean asBool() {
		return value;
	}

	@Override
	public byte asByte() {
		throw new ClassCastException("Cannot convert bool to byte");
	}

	@Override
	public short asShort() {
		throw new ClassCastException("Cannot convert bool to short");
	}

	@Override
	public int asInt() {
		throw new ClassCastException("Cannot convert bool to int");
	}

	@Override
	public long asLong() {
		throw new ClassCastException("Cannot convert bool to long");
	}

	@Override
	public float asFloat() {
		throw new ClassCastException("Cannot convert bool to double");
	}

	@Override
	public double asDouble() {
		throw new ClassCastException("Cannot convert bool to double");
	}

	@Override
	public String asString() {
		return Boolean.toString(value);
	}

	@Override
	public <T> T as(Class<T> cls) {
		return null;
	}

	@Override
	public boolean is(Class<?> cls) {
		return cls == boolean.class;
	}

	@Override
	public boolean canCastImplicit(Class<?> cls) {
		return false;
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.NONE;
	}

	@Override
	public Iterator<IAny> iteratorSingle() {
		throw new UnsupportedOperationException("bool doesn't have iterators");
	}

	@Override
	public Iterator<IAny[]> iteratorMulti(int n) {
		throw new UnsupportedOperationException("bool doesn't have iterators");
	}
}
