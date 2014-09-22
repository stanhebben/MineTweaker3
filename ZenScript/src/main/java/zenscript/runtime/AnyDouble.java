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
public class AnyDouble extends AnyNumber {
	private final double value;
	
	public AnyDouble(double value) {
		this.value = value;
	}
	
	@Override
	public IAny invert() {
		throw new UnsupportedOperationException("Cannot invert double values");
	}

	@Override
	public IAny neg() {
		return new AnyDouble(-value);
	}

	@Override
	public IAny add(IAny value) {
		return new AnyDouble(this.value + value.asDouble());
	}

	@Override
	public IAny sub(IAny value) {
		return new AnyDouble(this.value - value.asDouble());
	}

	@Override
	public IAny cat(IAny value) {
		return new AnyString(Double.toString(this.value) + value.asString());
	}

	@Override
	public IAny mul(IAny value) {
		return new AnyDouble(this.value * value.asDouble());
	}

	@Override
	public IAny div(IAny value) {
		return new AnyDouble(this.value / value.asDouble());
	}

	@Override
	public IAny mod(IAny value) {
		return new AnyDouble(this.value % value.asDouble());
	}

	@Override
	public IAny and(IAny value) {
		throw new UnsupportedOperationException("Cannot perform logic arithmetic on doubles");
	}

	@Override
	public IAny or(IAny value) {
		throw new UnsupportedOperationException("Cannot perform logic arithmetic on doubles");
	}

	@Override
	public IAny xor(IAny value) {
		throw new UnsupportedOperationException("Cannot perform logic arithmetic on doubles");
	}

	@Override
	public IAny range(IAny value) {
		throw new UnsupportedOperationException("Double doesn't support the range operator");
	}

	@Override
	public int compareTo(IAny value) {
		return Double.compare(this.value, value.asDouble());
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
		return (long) value;
	}

	@Override
	public float asFloat() {
		return (float) value;
	}

	@Override
	public double asDouble() {
		return value;
	}

	@Override
	public String asString() {
		return Double.toString(value);
	}

	@Override
	public boolean is(Class<?> cls) {
		return cls == double.class;
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.DOUBLE;
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
		hash = 97 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
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
		final AnyDouble other = (AnyDouble) obj;
		if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
			return false;
		}
		return true;
	}
}
