/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.util;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.JavaMethod;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingAnyBool;
import org.openzen.zencode.symbolic.type.casting.CastingAnyByte;
import org.openzen.zencode.symbolic.type.casting.CastingAnyDouble;
import org.openzen.zencode.symbolic.type.casting.CastingAnyFloat;
import org.openzen.zencode.symbolic.type.casting.CastingAnyInt;
import org.openzen.zencode.symbolic.type.casting.CastingAnyLong;
import org.openzen.zencode.symbolic.type.casting.CastingAnyShort;
import org.openzen.zencode.symbolic.type.casting.CastingAnyString;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;

/**
 *
 * @author Stan
 */
public class CommonMethods {
	public final IMethod BOOL_VALUEOF;
	public final IMethod BYTE_VALUEOF;
	public final IMethod SHORT_VALUEOF;
	public final IMethod INT_VALUEOF;
	public final IMethod LONG_VALUEOF;
	public final IMethod FLOAT_VALUEOF;
	public final IMethod DOUBLE_VALUEOF;
	
	public final IMethod BOOL_VALUE;
	public final IMethod BYTE_VALUE;
	public final IMethod SHORT_VALUE;
	public final IMethod INT_VALUE;
	public final IMethod LONG_VALUE;
	public final IMethod FLOAT_VALUE;
	public final IMethod DOUBLE_VALUE;
	
	public final IMethod BOOL_TOSTRING_STATIC;
	public final IMethod BYTE_TOSTRING_STATIC;
	public final IMethod SHORT_TOSTRING_STATIC;
	public final IMethod INT_TOSTRING_STATIC;
	public final IMethod LONG_TOSTRING_STATIC;
	public final IMethod FLOAT_TOSTRING_STATIC;
	public final IMethod DOUBLE_TOSTRING_STATIC;
	
	public final IMethod BOOL_TOSTRING;
	public final IMethod BYTE_TOSTRING;
	public final IMethod SHORT_TOSTRING;
	public final IMethod INT_TOSTRING;
	public final IMethod LONG_TOSTRING;
	public final IMethod FLOAT_TOSTRING;
	public final IMethod DOUBLE_TOSTRING;
	
	public final IMethod PARSE_BOOL;
	public final IMethod PARSE_BYTE;
	public final IMethod PARSE_SHORT;
	public final IMethod PARSE_INT;
	public final IMethod PARSE_LONG;
	public final IMethod PARSE_FLOAT;
	public final IMethod PARSE_DOUBLE;
	
	public final IMethod PARSE_BOOL_OBJECT;
	public final IMethod PARSE_BYTE_OBJECT;
	public final IMethod PARSE_SHORT_OBJECT;
	public final IMethod PARSE_INT_OBJECT;
	public final IMethod PARSE_LONG_OBJECT;
	public final IMethod PARSE_FLOAT_OBJECT;
	public final IMethod PARSE_DOUBLE_OBJECT;
	
	public final ICastingRule CAST_ANY_BOOL;
	public final ICastingRule CAST_ANY_BYTE;
	public final ICastingRule CAST_ANY_SHORT;
	public final ICastingRule CAST_ANY_INT;
	public final ICastingRule CAST_ANY_LONG;
	public final ICastingRule CAST_ANY_FLOAT;
	public final ICastingRule CAST_ANY_DOUBLE;
	public final ICastingRule CAST_ANY_STRING;
	
	public final IMethod METHOD_NOT;
	public final IMethod METHOD_NEG;
	public final IMethod METHOD_ADD;
	public final IMethod METHOD_CAT;
	public final IMethod METHOD_SUB;
	public final IMethod METHOD_MUL;
	public final IMethod METHOD_DIV;
	public final IMethod METHOD_MOD;
	public final IMethod METHOD_AND;
	public final IMethod METHOD_OR;
	public final IMethod METHOD_XOR;
	public final IMethod METHOD_RANGE;
	public final IMethod METHOD_COMPARETO;
	public final IMethod METHOD_CONTAINS;
	public final IMethod METHOD_MEMBERGET;
	public final IMethod METHOD_MEMBERSET;
	public final IMethod METHOD_MEMBERCALL;
	public final IMethod METHOD_INDEXGET;
	public final IMethod METHOD_INDEXSET;
	public final IMethod METHOD_CALL;
	public final IMethod METHOD_ASBOOL;
	public final IMethod METHOD_ASBYTE;
	public final IMethod METHOD_ASSHORT;
	public final IMethod METHOD_ASINT;
	public final IMethod METHOD_ASLONG;
	public final IMethod METHOD_ASFLOAT;
	public final IMethod METHOD_ASDOUBLE;
	public final IMethod METHOD_ASSTRING;
	public final IMethod METHOD_AS;
	public final IMethod METHOD_IS;
	public final IMethod METHOD_CANCASTIMPLICIT;
	public final IMethod METHOD_GETNUMBERTYPE;
	public final IMethod METHOD_ITERATORSINGLE;
	public final IMethod METHOD_ITERATORMULTI;
	
	public final IMethod METHOD_STRING_COMPARETO;
	
	public CommonMethods(IScopeGlobal environment) {
		TypeRegistry types = environment.getTypes();
		
		BOOL_VALUEOF = JavaMethod.get(types, Boolean.class, "valueOf", boolean.class);
		BYTE_VALUEOF = JavaMethod.get(types, Byte.class, "valueOf", byte.class);
		SHORT_VALUEOF = JavaMethod.get(types, Short.class, "valueOf", short.class);
		INT_VALUEOF = JavaMethod.get(types, Integer.class, "valueOf", int.class);
		LONG_VALUEOF = JavaMethod.get(types, Long.class, "valueOf", long.class);
		FLOAT_VALUEOF = JavaMethod.get(types, Float.class, "valueOf", float.class);
		DOUBLE_VALUEOF = JavaMethod.get(types, Double.class, "valueOf", double.class);
		
		BOOL_VALUE = JavaMethod.get(types, Boolean.class, "booleanValue");
		BYTE_VALUE = JavaMethod.get(types, Number.class, "byteValue");
		SHORT_VALUE = JavaMethod.get(types, Number.class, "shortValue");
		INT_VALUE = JavaMethod.get(types, Number.class, "intValue");
		LONG_VALUE = JavaMethod.get(types, Number.class, "longValue");
		FLOAT_VALUE = JavaMethod.get(types, Number.class, "floatValue");
		DOUBLE_VALUE = JavaMethod.get(types, Number.class, "doubleValue");
		
		BOOL_TOSTRING_STATIC = JavaMethod.get(types, Boolean.class, "toString", boolean.class);
		BYTE_TOSTRING_STATIC = JavaMethod.get(types, Byte.class, "toString", byte.class);
		SHORT_TOSTRING_STATIC = JavaMethod.get(types, Short.class, "toString", short.class);
		INT_TOSTRING_STATIC = JavaMethod.get(types, Integer.class, "toString", int.class);
		LONG_TOSTRING_STATIC = JavaMethod.get(types, Long.class, "toString", long.class);
		FLOAT_TOSTRING_STATIC = JavaMethod.get(types, Float.class, "toString", float.class);
		DOUBLE_TOSTRING_STATIC = JavaMethod.get(types, Double.class, "toString", double.class);
		
		BOOL_TOSTRING = JavaMethod.get(types, Boolean.class, "toString");
		BYTE_TOSTRING = JavaMethod.get(types, Byte.class, "toString");
		SHORT_TOSTRING = JavaMethod.get(types, Short.class, "toString");
		INT_TOSTRING = JavaMethod.get(types, Integer.class, "toString");
		LONG_TOSTRING = JavaMethod.get(types, Long.class, "toString");
		FLOAT_TOSTRING = JavaMethod.get(types, Float.class, "toString");
		DOUBLE_TOSTRING = JavaMethod.get(types, Double.class, "toString");
		
		PARSE_BOOL = JavaMethod.get(types, Boolean.class, "parseBoolean", String.class);
		PARSE_BYTE = JavaMethod.get(types, Byte.class, "parseByte", String.class);
		PARSE_SHORT = JavaMethod.get(types, Short.class, "parseShort", String.class);
		PARSE_INT = JavaMethod.get(types, Integer.class, "parseInt", String.class);
		PARSE_LONG = JavaMethod.get(types, Long.class, "parseLong", String.class);
		PARSE_FLOAT = JavaMethod.get(types, Float.class, "parseFloat", String.class);
		PARSE_DOUBLE = JavaMethod.get(types, Double.class, "parseDouble", String.class);
		
		PARSE_BOOL_OBJECT = JavaMethod.get(types, Boolean.class, "valueOf", String.class);
		PARSE_BYTE_OBJECT = JavaMethod.get(types, Byte.class, "valueOf", String.class);
		PARSE_SHORT_OBJECT = JavaMethod.get(types, Short.class, "valueOf", String.class);
		PARSE_INT_OBJECT = JavaMethod.get(types, Integer.class, "valueOf", String.class);
		PARSE_LONG_OBJECT = JavaMethod.get(types, Long.class, "valueOf", String.class);
		PARSE_FLOAT_OBJECT = JavaMethod.get(types, Float.class, "valueOf", String.class);
		PARSE_DOUBLE_OBJECT = JavaMethod.get(types, Double.class, "valueOf", String.class);
		
		METHOD_NOT = JavaMethod.get(types, IAny.class, "not");
		METHOD_NEG = JavaMethod.get(types, IAny.class, "neg");
		METHOD_ADD = JavaMethod.get(types, IAny.class, "add", IAny.class);
		METHOD_CAT = JavaMethod.get(types, IAny.class, "cat", IAny.class);
		METHOD_SUB = JavaMethod.get(types, IAny.class, "sub", IAny.class);
		METHOD_MUL = JavaMethod.get(types, IAny.class, "mul", IAny.class);
		METHOD_DIV = JavaMethod.get(types, IAny.class, "div", IAny.class);
		METHOD_MOD = JavaMethod.get(types, IAny.class, "mod", IAny.class);
		METHOD_AND = JavaMethod.get(types, IAny.class, "and", IAny.class);
		METHOD_OR = JavaMethod.get(types, IAny.class, "or", IAny.class);
		METHOD_XOR = JavaMethod.get(types, IAny.class, "xor", IAny.class);
		METHOD_RANGE = JavaMethod.get(types, IAny.class, "range", IAny.class);
		METHOD_COMPARETO = JavaMethod.get(types, IAny.class, "compareTo", IAny.class);
		METHOD_CONTAINS = JavaMethod.get(types, IAny.class, "contains", IAny.class);
		METHOD_MEMBERGET = JavaMethod.get(types, IAny.class, "memberGet", String.class);
		METHOD_MEMBERSET = JavaMethod.get(types, IAny.class, "memberSet", String.class, IAny.class);
		METHOD_MEMBERCALL = JavaMethod.get(types, IAny.class, "memberCall", String.class, IAny[].class);
		METHOD_INDEXGET = JavaMethod.get(types, IAny.class, "indexGet", IAny.class);
		METHOD_INDEXSET = JavaMethod.get(types, IAny.class, "indexSet", IAny.class, IAny.class);
		METHOD_CALL = JavaMethod.get(types, IAny.class, "call", IAny[].class);
		METHOD_ASBOOL = JavaMethod.get(types, IAny.class, "asBool");
		METHOD_ASBYTE = JavaMethod.get(types, IAny.class, "asByte");
		METHOD_ASSHORT = JavaMethod.get(types, IAny.class, "asShort");
		METHOD_ASINT = JavaMethod.get(types, IAny.class, "asInt");
		METHOD_ASLONG = JavaMethod.get(types, IAny.class, "asLong");
		METHOD_ASFLOAT = JavaMethod.get(types, IAny.class, "asFloat");
		METHOD_ASDOUBLE = JavaMethod.get(types, IAny.class, "asDouble");
		METHOD_ASSTRING = JavaMethod.get(types, IAny.class, "asString");
		METHOD_AS = JavaMethod.get(types, IAny.class, "as", Class.class);
		METHOD_IS = JavaMethod.get(types, IAny.class, "is", Class.class);
		METHOD_CANCASTIMPLICIT = JavaMethod.get(types, IAny.class, "canCastImplicit", Class.class);
		METHOD_GETNUMBERTYPE = JavaMethod.get(types, IAny.class, "getNumberType");
		METHOD_ITERATORSINGLE = JavaMethod.get(types, IAny.class, "iteratorSingle");
		METHOD_ITERATORMULTI = JavaMethod.get(types, IAny.class, "iteratorMulti", int.class);
		
		METHOD_STRING_COMPARETO = JavaMethod.get(types, String.class, "compareTo", int.class, String.class);
		
		CAST_ANY_BOOL = new CastingAnyBool(types);
		CAST_ANY_BYTE = new CastingAnyByte(types);
		CAST_ANY_SHORT = new CastingAnyShort(types);
		CAST_ANY_INT = new CastingAnyInt(types);
		CAST_ANY_LONG = new CastingAnyLong(types);
		CAST_ANY_FLOAT = new CastingAnyFloat(types);
		CAST_ANY_DOUBLE = new CastingAnyDouble(types);
		CAST_ANY_STRING = new CastingAnyString(types);
	}
}
