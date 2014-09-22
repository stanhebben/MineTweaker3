/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.runtime;

import java.util.Iterator;

/**
 *
 * @author Stan Hebben
 */
public class AnyNull implements IAny {
	public static final AnyNull INSTANCE = new AnyNull();
	
	private AnyNull() {}
	
	@Override
	public IAny not() {
		return AnyBool.TRUE;
	}

	@Override
	public IAny invert() {
		throw new NullPointerException();
	}

	@Override
	public IAny neg() {
		throw new NullPointerException();
	}

	@Override
	public IAny add(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny sub(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny cat(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny mul(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny div(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny mod(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny and(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny or(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny xor(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny range(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public int compareTo(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public boolean contains(IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny memberGet(String member) {
		throw new NullPointerException();
	}

	@Override
	public void memberSet(String member, IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny memberCall(String member, IAny... values) {
		throw new NullPointerException();
	}

	@Override
	public IAny indexGet(IAny key) {
		throw new NullPointerException();
	}

	@Override
	public void indexSet(IAny key, IAny value) {
		throw new NullPointerException();
	}

	@Override
	public IAny call(IAny... values) {
		throw new NullPointerException();
	}

	@Override
	public boolean asBool() {
		throw new NullPointerException();
	}

	@Override
	public byte asByte() {
		throw new NullPointerException();
	}

	@Override
	public short asShort() {
		throw new NullPointerException();
	}

	@Override
	public int asInt() {
		throw new NullPointerException();
	}

	@Override
	public long asLong() {
		throw new NullPointerException();
	}

	@Override
	public float asFloat() {
		throw new NullPointerException();
	}

	@Override
	public double asDouble() {
		throw new NullPointerException();
	}

	@Override
	public String asString() {
		throw new NullPointerException();
	}

	@Override
	public <T> T as(Class<T> cls) {
		return null;
	}

	@Override
	public boolean is(Class<?> cls) {
		return false;
	}

	@Override
	public boolean canCastImplicit(Class<?> cls) {
		return true;
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.NONE;
	}

	@Override
	public Iterator<IAny> iteratorSingle() {
		return null;
	}

	@Override
	public Iterator<IAny[]> iteratorMulti(int n) {
		return null;
	}
}
