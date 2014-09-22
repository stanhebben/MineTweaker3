/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.runtime;

import java.util.Iterator;

/**
 *
 * @author Stan
 */
public class AnyString implements IAny {
	private final String value;
	
	public AnyString(String value) {
		this.value = value;
	}
	
	@Override
	public IAny not() {
		throw new UnsupportedOperationException("Cannot perform not on a string");
	}

	@Override
	public IAny invert() {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny neg() {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny add(IAny value) {
		return cat(value);
	}

	@Override
	public IAny sub(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny cat(IAny value) {
		return new AnyString(this.value + value.asString());
	}

	@Override
	public IAny mul(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny div(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny mod(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny and(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny or(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny xor(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on a string");
	}

	@Override
	public IAny range(IAny value) {
		throw new UnsupportedOperationException("String doesn't support the range operator");
	}

	@Override
	public int compareTo(IAny value) {
		return this.value.compareTo(value.asString());
	}

	@Override
	public boolean contains(IAny value) {
		return this.value.contains(value.asString());
	}

	@Override
	public IAny memberGet(String member) {
		if (member.equals("length")) {
			return new AnyInt(this.value.length());
		} else {
			throw new UnsupportedOperationException("string doesn't have a member " + member);
		}
	}

	@Override
	public void memberSet(String member, IAny value) {
		throw new UnsupportedOperationException("string is immutable");
	}

	@Override
	public IAny memberCall(String member, IAny... values) {
		throw new UnsupportedOperationException("string doesn't have callable members");
	}

	@Override
	public IAny indexGet(IAny key) {
		int index = key.asInt();
		return new AnyString(this.value.substring(index, index + 1));
	}

	@Override
	public void indexSet(IAny key, IAny value) {
		throw new UnsupportedOperationException("string is immutable");
	}

	@Override
	public IAny call(IAny... values) {
		throw new UnsupportedOperationException("string cannot be called");
	}

	@Override
	public boolean asBool() {
		return Boolean.parseBoolean(value);
	}

	@Override
	public byte asByte() {
		return Byte.parseByte(value);
	}

	@Override
	public short asShort() {
		return Short.parseShort(value);
	}

	@Override
	public int asInt() {
		return Integer.parseInt(value);
	}

	@Override
	public long asLong() {
		return Long.parseLong(value);
	}

	@Override
	public float asFloat() {
		return Float.parseFloat(value);
	}

	@Override
	public double asDouble() {
		return Double.parseDouble(value);
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public <T> T as(Class<T> cls) {
		return null;
	}

	@Override
	public boolean is(Class<?> cls) {
		return cls == String.class;
	}

	@Override
	public boolean canCastImplicit(Class<?> cls) {
		return cls == String.class;
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.NONE;
	}

	@Override
	public Iterator<IAny> iteratorSingle() {
		return new CharacterIterator();
	}

	@Override
	public Iterator<IAny[]> iteratorMulti(int n) {
		throw new UnsupportedOperationException("iterator with " + n + " variables not supported");
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.value != null ? this.value.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AnyString other = (AnyString) obj;
		if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
	private class CharacterIterator implements Iterator<IAny> {
		private int index = 0;

		@Override
		public boolean hasNext() {
			return index < value.length();
		}

		@Override
		public IAny next() {
			IAny result = new AnyString(value.substring(index, index + 1));
			index++;
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("string is immutable");
		}
	}
}
