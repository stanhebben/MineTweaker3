/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.runtime;

import java.util.Iterator;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public class AnyInt implements IAny {
	private final int value;
	
	public AnyInt(int value) {
		this.value = value;
	}

	@Override
	public IAny not() {
		return new AnyInt(~value);
	}

	@Override
	public IAny neg() {
		return new AnyInt(-value);
	}

	@Override
	public IAny add(IAny value) {
		return new AnyInt(this.value + value.asInt());
	}

	@Override
	public IAny sub(IAny value) {
		return new AnyInt(this.value - value.asInt());
	}

	@Override
	public IAny cat(IAny value) {
		return new AnyString(String.valueOf(this.value) + value.asString());
	}

	@Override
	public IAny mul(IAny value) {
		return new AnyInt(this.value * value.asInt());
	}

	@Override
	public IAny div(IAny value) {
		return new AnyInt(this.value / value.asInt());
	}

	@Override
	public IAny mod(IAny value) {
		return new AnyInt(this.value % value.asInt());
	}

	@Override
	public IAny and(IAny value) {
		return new AnyInt(this.value & value.asInt());
	}

	@Override
	public IAny or(IAny value) {
		return new AnyInt(this.value | value.asInt());
	}

	@Override
	public IAny xor(IAny value) {
		return new AnyInt(this.value ^ value.asInt());
	}

	@Override
	public IAny range(IAny value) {
		// TODO: implement
		throw new UnsupportedOperationException("not yet supported");
	}

	@Override
	public int compareTo(IAny value) {
		return this.value - value.asInt();
	}

	@Override
	public boolean contains(IAny value) {
		return false;
	}

	@Override
	public IAny memberGet(String member) {
		throw new UnsupportedOperationException("No members in int");
	}

	@Override
	public void memberSet(String member, IAny value) {
		throw new UnsupportedOperationException("No members in int");
	}

	@Override
	public IAny memberCall(String member, IAny... values) {
		throw new UnsupportedOperationException("No members in int");
	}

	@Override
	public IAny indexGet(IAny key) {
		throw new UnsupportedOperationException("Cannot index integers");
	}

	@Override
	public void indexSet(IAny key, IAny value) {
		throw new UnsupportedOperationException("Cannot index integers");
	}

	@Override
	public IAny call(IAny... values) {
		throw new UnsupportedOperationException("Cannot call integers");
	}

	@Override
	public boolean asBool() {
		throw new UnsupportedOperationException("Cannot convert integers to bools");
	}

	@Override
	public byte asByte() {
		return (byte) value;
	}

	@Override
	public short asShort() {
		return (short) value;
	}

	@Override
	public int asInt() {
		return value;
	}

	@Override
	public long asLong() {
		return value;
	}

	@Override
	public float asFloat() {
		return value;
	}

	@Override
	public double asDouble() {
		return value;
	}

	@Override
	public String asString() {
		return Integer.toString(value);
	}

	@Override
	public <T> T as(Class<T> cls) {
		return null;
	}

	@Override
	public boolean is(Class<?> cls) {
		return cls == int.class;
	}

	@Override
	public boolean canCastImplicit(Class<?> cls) {
		return cls == int.class || cls == byte.class || cls == short.class || cls == float.class || cls == double.class;
	}

	@Override
	public int getNumberType() {
		return ZenType.NUM_INT;
	}

	@Override
	public Iterator<IAny> iteratorSingle() {
		throw new UnsupportedOperationException("int doesn't have iterators");
	}

	@Override
	public Iterator<IAny[]> iteratorMulti(int n) {
		throw new UnsupportedOperationException("int doesn't have iterators");
	}
}
