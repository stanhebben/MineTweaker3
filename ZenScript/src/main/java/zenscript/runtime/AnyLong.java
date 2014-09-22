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
public class AnyLong extends AnyNumber {
	private final long value;
	
	public AnyLong(long value) {
		this.value = value;
	}

	@Override
	public IAny invert() {
		return new AnyLong(~value);
	}

	@Override
	public IAny neg() {
		return new AnyLong(-value);
	}

	@Override
	public IAny add(IAny value) {
		if (value.getNumberType() == NumberType.FLOAT || value.getNumberType() == NumberType.DOUBLE) {
			return new AnyDouble(this.value + value.asDouble());
		} else if (value.getNumberType() == NumberType.NONE) {
			return new AnyString(this.value + value.asString());
		} else {
			return new AnyLong(this.value + value.asLong());
		}
	}

	@Override
	public IAny sub(IAny value) {
		if (value.getNumberType() == NumberType.FLOAT || value.getNumberType() == NumberType.DOUBLE) {
			return new AnyDouble(this.value - value.asDouble());
		} else if (value.getNumberType() == NumberType.NONE) {
			throw new UnsupportedOperationException("Cannot subtract non-number values from long values");
		} else {
			return new AnyLong(this.value - value.asLong());
		}
	}

	@Override
	public IAny cat(IAny value) {
		return new AnyString(this.value + value.asString());
	}

	@Override
	public IAny mul(IAny value) {
		if (value.getNumberType() == NumberType.FLOAT || value.getNumberType() == NumberType.DOUBLE) {
			return new AnyDouble(this.value * value.asDouble());
		} else if (value.getNumberType() == NumberType.NONE) {
			throw new UnsupportedOperationException("Cannot multiply non-number values with long values");
		} else {
			return new AnyLong(this.value * value.asLong());
		}
	}

	@Override
	public IAny div(IAny value) {
		if (value.getNumberType() == NumberType.FLOAT || value.getNumberType() == NumberType.DOUBLE) {
			return new AnyDouble(this.value / value.asDouble());
		} else if (value.getNumberType() == NumberType.NONE) {
			throw new UnsupportedOperationException("Cannot divide long values by non-number values");
		} else {
			return new AnyLong(this.value / value.asLong());
		}
	}

	@Override
	public IAny mod(IAny value) {
		if (value.getNumberType() == NumberType.FLOAT || value.getNumberType() == NumberType.DOUBLE) {
			return new AnyDouble(this.value % value.asDouble());
		} else if (value.getNumberType() == NumberType.NONE) {
			throw new UnsupportedOperationException("Cannot modulo long values with  non-number values");
		} else {
			return new AnyLong(this.value % value.asLong());
		}
	}

	@Override
	public IAny and(IAny value) {
		return new AnyLong(this.value & value.asLong());
	}

	@Override
	public IAny or(IAny value) {
		return new AnyLong(this.value | value.asLong());
	}

	@Override
	public IAny xor(IAny value) {
		return new AnyLong(this.value ^ value.asLong());
	}

	@Override
	public IAny range(IAny value) {
		if (this.value < Integer.MAX_VALUE) {
			return new AnyRange((int) this.value, value.asInt());
		} else {
			throw new NumberFormatException("value too large for a range");
		}
	}

	@Override
	public int compareTo(IAny value) {
		switch (value.getNumberType()) {
			case BYTE:
			case SHORT:
			case INT:
			case LONG: {
				long lValue = value.asLong();
				return Long.compare(this.value, lValue);
			}
			case FLOAT:
			case DOUBLE:
				double dValue = value.asDouble();
				return Double.compare(this.value, dValue);
			default:
				throw new AssertionError("Invalid number type: " + value);
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
		return (int) value;
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
		return Long.toString(value);
	}

	@Override
	public boolean is(Class<?> cls) {
		return cls == long.class;
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.LONG;
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
		int hash = 7;
		hash = 83 * hash + (int) (this.value ^ (this.value >>> 32));
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
		final AnyLong other = (AnyLong) obj;
		if (this.value != other.value) {
			return false;
		}
		return true;
	}
}
