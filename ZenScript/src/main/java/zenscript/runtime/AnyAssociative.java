/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Stan
 */
public class AnyAssociative implements IAny {
	private final Map<IAny, IAny> values;
	
	public AnyAssociative(Map<IAny, IAny> values) {
		this.values = values;
	}

	@Override
	public IAny not() {
		throw new UnsupportedOperationException("Cannot perform not on an associative array");
	}

	@Override
	public IAny invert() {
		throw new UnsupportedOperationException("Cannot invert an associative array");
	}

	@Override
	public IAny neg() {
		throw new UnsupportedOperationException("Cannot negate an associative array");
	}

	@Override
	public IAny add(IAny value) {
		Map<IAny, IAny> map = value.as(Map.class);
		if (map == null) {
			throw new UnsupportedOperationException("Add only works with associative arrays");
		} else {
			Map<IAny, IAny> copy = new HashMap<IAny, IAny>();
			
			for (Map.Entry<IAny, IAny> entry : this.values.entrySet()) {
				copy.put(entry.getKey(), entry.getValue());
			}
			for (Map.Entry<IAny, IAny> entry : map.entrySet()) {
				copy.put(entry.getKey(), entry.getValue());
			}
			
			return new AnyAssociative(copy);
		}
	}

	@Override
	public IAny sub(IAny value) {
		Map<IAny, IAny> map = value.as(Map.class);
		if (map == null) {
			throw new UnsupportedOperationException("Subtract only works with associative arrays");
		} else {
			Map<IAny, IAny> copy = new HashMap<IAny, IAny>();
			
			for (Map.Entry<IAny, IAny> entry : this.values.entrySet()) {
				copy.put(entry.getKey(), entry.getValue());
			}
			for (IAny key : map.keySet()) {
				copy.remove(key);
			}
			
			return new AnyAssociative(copy);
		}
	}

	@Override
	public IAny cat(IAny value) {
		throw new UnsupportedOperationException("Cannot concatenate associative arrays");
	}

	@Override
	public IAny mul(IAny value) {
		throw new UnsupportedOperationException("Cannot multiply associative arrays");
	}

	@Override
	public IAny div(IAny value) {
		throw new UnsupportedOperationException("Cannot divide associative arrays");
	}

	@Override
	public IAny mod(IAny value) {
		throw new UnsupportedOperationException("Cannot modulo associative arrays");
	}

	@Override
	public IAny and(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on associative arrays");
	}

	@Override
	public IAny or(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on associative arrays");
	}

	@Override
	public IAny xor(IAny value) {
		throw new UnsupportedOperationException("Cannot perform arithmetic on associative arrays");
	}

	@Override
	public IAny range(IAny value) {
		throw new UnsupportedOperationException("Associative arrays don't have a range operator");
	}

	@Override
	public int compareTo(IAny value) {
		throw new UnsupportedOperationException("Cannot compare associative arrays");
	}

	@Override
	public boolean contains(IAny value) {
		return values.containsKey(value);
	}

	@Override
	public IAny memberGet(String member) {
		return values.get(new AnyString(member));
	}

	@Override
	public void memberSet(String member, IAny value) {
		values.put(new AnyString(member), value);
	}

	@Override
	public IAny memberCall(String member, IAny... values) {
		IAny memberValue = memberGet(member);
		if (memberValue == null)
			throw new UnknownMemberException("any[any]", member);
		
		return memberValue.call(values);
	}

	@Override
	public IAny indexGet(IAny key) {
		return values.get(key);
	}

	@Override
	public void indexSet(IAny key, IAny value) {
		values.put(key, value);
	}

	@Override
	public IAny call(IAny... values) {
		throw new UnsupportedOperationException("Cannot call associative arrays");
	}

	@Override
	public boolean asBool() {
		throw new ClassCastException("Cannot cast associative array to bool");
	}

	@Override
	public byte asByte() {
		throw new ClassCastException("Cannot cast associative array to byte");
	}

	@Override
	public short asShort() {
		throw new ClassCastException("Cannot cast associative array to short");
	}

	@Override
	public int asInt() {
		throw new ClassCastException("Cannot cast associative array to int");
	}

	@Override
	public long asLong() {
		throw new ClassCastException("Cannot cast associative array to long");
	}

	@Override
	public float asFloat() {
		throw new ClassCastException("Cannot cast associative array to float");
	}

	@Override
	public double asDouble() {
		throw new ClassCastException("Cannot cast associative array to double");
	}

	@Override
	public String asString() {
		throw new ClassCastException("Cannot cast associative array to string");
	}

	@Override
	public <T> T as(Class<T> cls) {
		if (cls == Map.class)
			return (T) values;
		
		return null;
	}

	@Override
	public boolean is(Class<?> cls) {
		return cls == Map.class;
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
		return values.keySet().iterator();
	}

	@Override
	public Iterator<IAny[]> iteratorMulti(int n) {
		if (n == 2) {
			return new KeyValueIterator();
		} else {
			return null;
		}
	}
	
	private class KeyValueIterator implements Iterator<IAny[]> {
		Iterator<Map.Entry<IAny, IAny>> baseIterator;
		IAny[] array = new IAny[2];
		
		public KeyValueIterator() {
			baseIterator = values.entrySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return baseIterator.hasNext();
		}

		@Override
		public IAny[] next() {
			Map.Entry<IAny, IAny> value = baseIterator.next();
			array[0] = value.getKey();
			array[1] = value.getValue();
			return array;
		}

		@Override
		public void remove() {
			baseIterator.remove();
		}
	}
}
