/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.runtime;

/**
 *
 * @author Stan
 */
public abstract class AnyNumber implements IAny {
	@Override
	public final IAny not() {
		throw new UnsupportedOperationException("numbers don't support the ! operator");
	}

	@Override
	public final boolean contains(IAny value) {
		return false;
	}

	@Override
	public final IAny memberGet(String member) {
		throw new UnknownMemberException(member, "number");
	}

	@Override
	public final void memberSet(String member, IAny value) {
		throw new ImmutableException("Numbers don't have settable members");
	}

	@Override
	public final IAny memberCall(String member, IAny... values) {
		throw new UnsupportedOperationException("Numbers don't have callable members");
	}

	@Override
	public final IAny indexGet(IAny key) {
		throw new UnsupportedOperationException("Cannot index number values");
	}

	@Override
	public final void indexSet(IAny key, IAny value) {
		throw new UnsupportedOperationException("Cannot index number values");
	}

	@Override
	public final IAny call(IAny... values) {
		throw new UnsupportedOperationException("Cannot call number values");
	}

	@Override
	public final boolean asBool() {
		throw new ClassCastException("Cannot cast numbers to bool values");
	}

	@Override
	public final <T> T as(Class<T> cls) {
		if (cls == Byte.class) {
			return (T) Byte.valueOf(asByte());
		} else if (cls == Short.class) {
			return (T) Short.valueOf(asShort());
		} else if (cls == Integer.class) {
			return (T) Integer.valueOf(asInt());
		} else if (cls == Long.class) {
			return (T) Long.valueOf(asLong());
		} else if (cls == Float.class) {
			return (T) Float.valueOf(asFloat());
		} else if (cls == Double.class) {
			return (T) Double.valueOf(asDouble());
		} else if (cls == String.class) {
			return (T) asString();
		} else {
			return null;
		}
	}
	
	@Override
	public final boolean canCastImplicit(Class cls) {
		return cls == byte.class || cls == short.class || cls == int.class || cls == long.class
				|| cls == float.class || cls == double.class
				|| cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class
				|| cls == Float.class || cls == Double.class;
	}
}
