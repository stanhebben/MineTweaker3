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
public class AnyRange implements IAny {
	private final int from;
	private final int to;
	
	public AnyRange(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public IAny not() {
		throw new UnsupportedOperationException("Cannot perform not on ranges");
	}

	@Override
	public IAny invert() {
		throw new UnsupportedOperationException("Cannot invert ranges");
	}

	@Override
	public IAny neg() {
		throw new UnsupportedOperationException("Cannot negate ranges");
	}

	@Override
	public IAny add(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny sub(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny cat(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny mul(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny div(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny mod(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny and(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny or(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny xor(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on ranges");
	}

	@Override
	public IAny range(IAny value) {
		throw new UnsupportedOperationException("Cannot perform the range operator on ranges");
	}

	@Override
	public int compareTo(IAny value) {
		throw new UnsupportedOperationException("Cannot compare ranges");
	}

	@Override
	public boolean contains(IAny value) {
		Range range = value.as(Range.class);
		if (range == null) {
			return false;
		} else {
			return range.getFrom() >= from && range.getTo() <= to;
		}
	}

	@Override
	public IAny memberGet(String member) {
		if (member.equals("from")) {
			return new AnyInt(from);
		} else if (member.equals("to")) {
			return new AnyInt(to);
		} else {
			throw new UnknownMemberException(member, "range");
		}
	}

	@Override
	public void memberSet(String member, IAny value) {
		throw new UnsupportedOperationException("range is immutable");
	}

	@Override
	public IAny memberCall(String member, IAny... values) {
		throw new UnsupportedOperationException("range doesn't have callable members");
	}

	@Override
	public IAny indexGet(IAny key) {
		int iKey = key.asInt();
		if (iKey < 0 || iKey >= (to - from))
			return new AnyInt(iKey + to);
		
		throw new ArrayIndexOutOfBoundsException(iKey);
	}

	@Override
	public void indexSet(IAny key, IAny value) {
		throw new UnsupportedOperationException("range is immutable");
	}

	@Override
	public IAny call(IAny... values) {
		throw new UnsupportedOperationException("Cannot call ranges");
	}

	@Override
	public boolean asBool() {
		throw new ClassCastException("Cannot cast range to bool");
	}

	@Override
	public byte asByte() {
		throw new ClassCastException("Cannot cast range to byte");
	}

	@Override
	public short asShort() {
		throw new ClassCastException("Cannot cast range to short");
	}

	@Override
	public int asInt() {
		throw new ClassCastException("Cannot cast range to int");
	}

	@Override
	public long asLong() {
		throw new ClassCastException("Cannot cast range to long");
	}

	@Override
	public float asFloat() {
		throw new ClassCastException("Cannot cast range to float");
	}

	@Override
	public double asDouble() {
		throw new ClassCastException("Cannot cast range to double");
	}

	@Override
	public String asString() {
		return from + ".." + to;
	}

	@Override
	public <T> T as(Class<T> cls) {
		if (cls == Range.class) {
			return (T) new Range(from, to);
		} else {
			return null;
		}
	}

	@Override
	public boolean is(Class<?> cls) {
		return cls == Range.class;
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
		return null;
	}

	@Override
	public Iterator<IAny[]> iteratorMulti(int n) {
		return null;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + this.from;
		hash = 89 * hash + this.to;
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
		final AnyRange other = (AnyRange) obj;
		if (this.from != other.from) {
			return false;
		}
		if (this.to != other.to) {
			return false;
		}
		return true;
	}
}
