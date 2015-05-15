/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Stan
 */
public class AnyArray implements IAny {
	public static final AnyArray EMPTY = new AnyArray(Collections.emptyList());
	
	private final List<IAny> values;
	
	public AnyArray(List<IAny> values) {
		this.values = values;
	}
	
	@Override
	public IAny not() {
		throw new UnsupportedOperationException("Cannot perform not on arrays");
	}

	@Override
	public IAny invert() {
		throw new UnsupportedOperationException("Cannot invert arrays");
	}

	@Override
	public IAny neg() {
		throw new UnsupportedOperationException("Cannot negate arrays");
	}

	@Override
	public IAny add(IAny value) {
		return cat(value);
	}

	@Override
	public IAny sub(IAny value) {
		throw new UnsupportedOperationException("Cannot subtract from arrays");
	}

	@Override
	public IAny cat(IAny value) {
		Iterator<IAny> iterator = value.iteratorSingle();
		if (iterator == null) {
			List<IAny> newValues = new ArrayList<>();
			newValues.addAll(values);
			newValues.add(value);
			return new AnyArray(newValues);
		} else {
			List<IAny> newValues = new ArrayList<>();
			newValues.addAll(this.values);
			while (iterator.hasNext()) {
				newValues.add(iterator.next());
			}
			return new AnyArray(newValues);
		}
	}

	@Override
	public IAny mul(IAny value) {
		throw new UnsupportedOperationException("Cannot multiply arrays");
	}

	@Override
	public IAny div(IAny value) {
		throw new UnsupportedOperationException("Cannot divide arrays");
	}

	@Override
	public IAny mod(IAny value) {
		throw new UnsupportedOperationException("Cannot modulo arrays");
	}

	@Override
	public IAny and(IAny value) {
		throw new UnsupportedOperationException("Arrays don't support logic arithmetic");
	}

	@Override
	public IAny or(IAny value) {
		throw new UnsupportedOperationException("Arrays don't support logic arithmetic");
	}

	@Override
	public IAny xor(IAny value) {
		throw new UnsupportedOperationException("Arrays don't support logic arithmetic");
	}

	@Override
	public IAny range(IAny value) {
		throw new UnsupportedOperationException("Arrays don't support range arithmetic");
	}

	@Override
	public int compareTo(IAny value) {
		throw new UnsupportedOperationException("Can't compare arrays");
	}

	@Override
	public boolean contains(IAny value) {
		for (IAny arrayValue : values) {
			if (arrayValue.equals(value))
				return true;
		}
		
		return false;
	}

	@Override
	public IAny memberGet(String member) {
		if (member.equals("length")) {
			return new AnyInt(values.size());
		} else {
			throw new UnsupportedOperationException("Arrays don't have a " + member + " member");
		}
	}

	@Override
	public void memberSet(String member, IAny value) {
		throw new UnsupportedOperationException("Arrays don't have settable members");
	}

	@Override
	public IAny memberCall(String member, IAny... values) {
		throw new UnsupportedOperationException("Arrays don't have callable members");
	}

	@Override
	public IAny indexGet(IAny key) {
		return values.get(key.asInt());
	}

	@Override
	public void indexSet(IAny key, IAny value) {
		values.set(key.asInt(), value);
	}

	@Override
	public IAny call(IAny... values) {
		throw new UnsupportedOperationException("Can't call arrays");
	}

	@Override
	public boolean asBool() {
		throw new ClassCastException("Can't cast arrays to bool values");
	}

	@Override
	public byte asByte() {
		throw new ClassCastException("Can't cast arrays to byte values");
	}

	@Override
	public short asShort() {
		throw new ClassCastException("Can't cast arrays to short values");
	}

	@Override
	public int asInt() {
		throw new ClassCastException("Can't cast arrays to int values");
	}

	@Override
	public long asLong() {
		throw new ClassCastException("Can't cast arrays to long values");
	}

	@Override
	public float asFloat() {
		throw new ClassCastException("Can't cast arrays to float values");
	}

	@Override
	public double asDouble() {
		throw new ClassCastException("Can't cast arrays to double values");
	}

	@Override
	public String asString() {
		StringBuilder result = new StringBuilder();
		result.append("[");
		
		boolean first = true;
		for (IAny value : values) {
			if (first) {
				first = false;
			} else {
				result.append(", ");
			}
			
			result.append(value.asString());
		}
		result.append("]");
		
		return result.toString();
	}

	@Override
	public <T> T as(Class<T> cls) {
		if (cls == IAny[].class) {
			return (T) values;
		} else {
			throw new ClassCastException("Cannot cast arrays to another class");
		}
	}

	@Override
	public boolean is(Class<?> cls) {
		return cls == IAny[].class;
	}

	@Override
	public boolean canCastImplicit(Class<?> cls) {
		return is(cls);
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.NONE;
	}

	@Override
	public Iterator<IAny> iteratorSingle() {
		return values.iterator();
	}

	@Override
	public Iterator<IAny[]> iteratorMulti(int n) {
		return null;
	}
}
