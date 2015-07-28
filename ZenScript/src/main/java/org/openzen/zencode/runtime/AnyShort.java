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
public class AnyShort extends AnyNumber {
	private final int value;
	
	public AnyShort(int value) {
		this.value = value;
	}
	
	@Override
	public IAny invert() {
		return new AnyShort(~value);
	}

	@Override
	public IAny neg() {
		return new AnyShort(-value);
	}

	@Override
	public IAny add(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
				return new AnyShort(this.value + value.asShort());
			case INT:
				return new AnyInt(this.value + value.asInt());
			case LONG:
				return new AnyLong(this.value + value.asLong());
			case FLOAT:
			case DOUBLE:
				return new AnyDouble(this.value + value.asDouble());
			case NONE:
				return new AnyString(this.value + value.asString());
			default:
				throw new AssertionError("Invalid number type: " + value.getNumberType());
		}
	}

	@Override
	public IAny sub(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
				return new AnyShort(this.value - value.asShort());
			case INT:
				return new AnyInt(this.value - value.asInt());
			case LONG:
				return new AnyLong(this.value - value.asLong());
			case FLOAT:
			case DOUBLE:
				return new AnyDouble(this.value - value.asDouble());
			case NONE:
				return new AnyShort(this.value - value.asInt());
			default:
				throw new AssertionError("Invalid number type: " + value.getNumberType());
		}
	}

	@Override
	public IAny cat(IAny value) {
		return new AnyString(String.valueOf(this.value) + value.asString());
	}

	@Override
	public IAny mul(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
				return new AnyShort(this.value * value.asShort());
			case INT:
				return new AnyInt(this.value * value.asInt());
			case LONG:
				return new AnyLong(this.value * value.asLong());
			case FLOAT:
			case DOUBLE:
				return new AnyDouble(this.value * value.asDouble());
			case NONE:
				return new AnyShort(this.value * value.asInt());
			default:
				throw new AssertionError("Invalid number type: " + value.getNumberType());
		}
	}

	@Override
	public IAny div(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
				return new AnyShort(this.value / value.asShort());
			case INT:
				return new AnyInt(this.value / value.asInt());
			case LONG:
				return new AnyLong(this.value / value.asLong());
			case FLOAT:
			case DOUBLE:
				return new AnyDouble(this.value / value.asDouble());
			case NONE:
				return new AnyShort(this.value / value.asInt());
			default:
				throw new AssertionError("Invalid number type: " + value.getNumberType());
		}
	}

	@Override
	public IAny mod(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
				return new AnyShort(this.value % value.asShort());
			case INT:
				return new AnyShort(this.value % value.asInt());
			case LONG:
				return new AnyLong(this.value % value.asLong());
			case FLOAT:
			case DOUBLE:
				return new AnyDouble(this.value % value.asDouble());
			case NONE:
				return new AnyShort(this.value % value.asInt());
			default:
				throw new AssertionError("Invalid number type: " + value.getNumberType());
		}
	}

	@Override
	public IAny and(IAny value) {
		return new AnyShort(this.value & value.asInt());
	}

	@Override
	public IAny or(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
				return new AnyShort(this.value | value.asShort());
			case INT:
				return new AnyInt(this.value | value.asInt());
			case LONG:
				return new AnyLong(this.value | value.asLong());
			case FLOAT:
			case DOUBLE:
				throw new UnsupportedOperationException("Cannot | with a double");
			case NONE:
				return new AnyShort(this.value | value.asInt());
			default:
				throw new AssertionError("Invalid number type: " + value.getNumberType());
		}
	}

	@Override
	public IAny xor(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
				return new AnyShort(this.value ^ value.asShort());
			case INT:
				return new AnyShort(this.value ^ value.asInt());
			case LONG:
				return new AnyLong(this.value ^ value.asLong());
			case FLOAT:
			case DOUBLE:
				throw new UnsupportedOperationException("Cannot ^ with a double");
			case NONE:
				return new AnyShort(this.value ^ value.asInt());
			default:
				throw new AssertionError("Invalid number type: " + value.getNumberType());
		}
	}

	@Override
	public IAny range(IAny value) {
		return new AnyRange(this.value, value.asInt());
	}

	@Override
	public int compareTo(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
			case INT:
				return this.value - value.asInt();
			case LONG:
				return Long.compare(this.value, value.asLong());
			case FLOAT:
			case DOUBLE:
				return Double.compare(this.value, value.asDouble());
			case NONE:
				return this.value - value.asInt();
			default:
				throw new AssertionError("Invalid number type: " + value.getNumberType());
		}
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
	public boolean is(Class<?> cls) {
		return cls == int.class;
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.INT;
	}

	@Override
	public Iterator<IAny> iteratorSingle() {
		return null;
	}

	@Override
	public Iterator<IAny[]> iteratorMulti(int n) {
		return null;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 17 * hash + this.value;
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
		final AnyShort other = (AnyShort) obj;
		if (this.value != other.value) {
			return false;
		}
		return true;
	}
}
