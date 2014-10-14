/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAny;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import stanhebben.zenscript.type.ZenTypeArrayList;
import stanhebben.zenscript.type.ZenTypeAssociative;
import stanhebben.zenscript.type.ZenTypeBool;
import stanhebben.zenscript.type.ZenTypeBoolObject;
import stanhebben.zenscript.type.ZenTypeByte;
import stanhebben.zenscript.type.ZenTypeByteObject;
import stanhebben.zenscript.type.ZenTypeDouble;
import stanhebben.zenscript.type.ZenTypeDoubleObject;
import stanhebben.zenscript.type.ZenTypeFloat;
import stanhebben.zenscript.type.ZenTypeFloatObject;
import stanhebben.zenscript.type.ZenTypeInt;
import stanhebben.zenscript.type.ZenTypeIntObject;
import stanhebben.zenscript.type.ZenTypeLong;
import stanhebben.zenscript.type.ZenTypeLongObject;
import stanhebben.zenscript.type.ZenTypeNative;
import stanhebben.zenscript.type.ZenTypeNull;
import stanhebben.zenscript.type.ZenTypeShort;
import stanhebben.zenscript.type.ZenTypeShortObject;
import stanhebben.zenscript.type.ZenTypeString;
import stanhebben.zenscript.type.ZenTypeVoid;
import zenscript.runtime.IAny;
import zenscript.runtime.Range;
import zenscript.symbolic.type.generic.TypeCapture;
import zenscript.symbolic.type.generic.TypeVariableNative;
import zenscript.symbolic.util.CommonMethods;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public final class TypeRegistry {
	public final ZenType VOID;
	public final ZenType NULL;
	public final ZenType ANY;
	public final ZenType BOOL;
	public final ZenType BOOLOBJECT;
	public final ZenType BYTE;
	public final ZenType BYTEOBJECT;
	public final ZenType SHORT;
	public final ZenType SHORTOBJECT;
	public final ZenType INT;
	public final ZenType INTOBJECT;
	public final ZenType LONG;
	public final ZenType LONGOBJECT;
	public final ZenType FLOAT;
	public final ZenType FLOATOBJECT;
	public final ZenType DOUBLE;
	public final ZenType DOUBLEOBJECT;
	public final ZenType STRING;
	public final ZenType RANGE;
	
	public final ZenTypeArrayBasic ANYARRAY;
	public final ZenTypeArrayList ANYARRAYLIST;
	public final ZenTypeAssociative ANYMAP;
	
	private final IScopeGlobal environment;
	private final Map<Class, ZenType> nativeTypes;
	private final CommonMethods commonMethods;
	private final IScopeMethod staticGlobalEnvironment;
	
	public TypeRegistry(IScopeGlobal environment) {
		this.environment = environment;
		
		VOID = new ZenTypeVoid(environment);
		NULL = new ZenTypeNull(environment);
		ANY = new ZenTypeAny(environment);
		BOOL = new ZenTypeBool(environment);
		BOOLOBJECT = new ZenTypeBoolObject(environment, BOOL);
		BYTE = new ZenTypeByte(environment);
		BYTEOBJECT = new ZenTypeByteObject(environment, BYTE);
		SHORT = new ZenTypeShort(environment);
		SHORTOBJECT = new ZenTypeShortObject(environment, SHORT);
		INT = new ZenTypeInt(environment);
		INTOBJECT = new ZenTypeIntObject(environment, INT);
		LONG = new ZenTypeLong(environment);
		LONGOBJECT = new ZenTypeLongObject(environment, LONG);
		FLOAT = new ZenTypeFloat(environment);
		FLOATOBJECT = new ZenTypeFloatObject(environment, FLOAT);
		DOUBLE = new ZenTypeDouble(environment);
		DOUBLEOBJECT = new ZenTypeDoubleObject(environment, DOUBLE);
		STRING = new ZenTypeString(environment);
		
		ANYARRAY = new ZenTypeArrayBasic(ANY);
		ANYARRAYLIST = new ZenTypeArrayList(ANY);
		ANYMAP = new ZenTypeAssociative(ANY, ANY);
		
		nativeTypes = new HashMap<Class, ZenType>();
		
		nativeTypes.put(boolean.class, BOOL);
		nativeTypes.put(byte.class, BYTE);
		nativeTypes.put(short.class, SHORT);
		nativeTypes.put(int.class, INT);
		nativeTypes.put(long.class, LONG);
		nativeTypes.put(float.class, FLOAT);
		nativeTypes.put(double.class, DOUBLE);
		nativeTypes.put(void.class, VOID);
		
		nativeTypes.put(Boolean.class, BOOLOBJECT);
		nativeTypes.put(Byte.class, BYTEOBJECT);
		nativeTypes.put(Short.class, SHORTOBJECT);
		nativeTypes.put(Integer.class, INTOBJECT);
		nativeTypes.put(Long.class, LONGOBJECT);
		nativeTypes.put(Float.class, FLOATOBJECT);
		nativeTypes.put(Double.class, DOUBLEOBJECT);
		
		nativeTypes.put(IAny.class, ANY);
		
		nativeTypes.put(String.class, STRING);
		nativeTypes.put(List.class, ANYARRAYLIST);
		
		this.commonMethods = new CommonMethods(environment);
		staticGlobalEnvironment = new StaticGlobalEnvironment();
		
		RANGE = getNativeType(null, Range.class, null);
	}
	
	public ZenType getNativeType(ZenPosition position, Type type, TypeCapture capture) {
		if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			Type raw = pType.getRawType();
			if (raw instanceof Class) {
				Class rawClass = (Class) raw;
				TypeVariable[] typeVariables = rawClass.getTypeParameters();
				if (typeVariables.length != 0) {
					environment.error(position, "Missing type parameters");
				}
				
				if (List.class == rawClass) {
					return getListType(pType);
				} else if (Map.class == rawClass) {
					return getMapType(pType);
				} else {
					return getNativeClassType(position, rawClass, capture);
				}
			} else {
				return getNativeType(position, raw, capture);
			}
		} else if (type instanceof Class) {
			return getNativeClassType(position, (Class) type, capture);
		} else {
			throw new RuntimeException("Could not convert native type: " + type);
		}
	}
	
	public ZenType getInstancedNativeType(ZenPosition position, Type type, List<ZenType> genericTypes, TypeCapture capture) {
		if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			Type raw = pType.getRawType();
			if (raw instanceof Class) {
				Class rawCls = (Class) raw;
				
				if (List.class == rawCls) {
					return getListType(pType);
				} else if (Map.class == rawCls) {
					return getMapType(pType);
				} else {
					Type[] parameters = pType.getActualTypeArguments();
					TypeVariable[] typeVariables = rawCls.getTypeParameters();
					TypeCapture newCapture = new TypeCapture(capture);
					for (int i = 0; i < parameters.length; i++) {
						newCapture.put(new TypeVariableNative(
								typeVariables[i]),
								getNativeType(position, parameters[i], capture)
						);
					}

					return getNativeClassType(position, rawCls, newCapture);
				}
			} else {
				return getNativeType(position, raw, capture);
			}
		} else if (type instanceof Class) {
			Class cls = (Class) type;
			
			TypeVariable[] typeVariables = cls.getTypeParameters();
			if (typeVariables.length != genericTypes.size()) {
				environment.error(position, "number of parameters doesn't match");
				return getNativeType(position, type, capture);
			}
			
			TypeCapture newCapture = new TypeCapture(capture);
			for (int i = 0; i < typeVariables.length; i++) {
				newCapture.put(new TypeVariableNative(typeVariables[i]), genericTypes.get(i));
			}
			
			return getNativeClassType(position, cls, newCapture);
		} else {
			throw new RuntimeException("Could not convert native type: " + type);
		}
	}
	
	public CommonMethods getCommonMethods() {
		return commonMethods;
	}
	
	/**
	 * Retrieves a method environment that can be used to resolve constant
	 * expressions.
	 * 
	 * @return compile-time static environment
	 */
	public IScopeMethod getStaticGlobalEnvironment() {
		return staticGlobalEnvironment;
	}
	
	// #######################
	// ### Private methods ###
	// #######################
	
	private ZenType getNativeClassType(ZenPosition position, Class cls, TypeCapture capture) {
		if (nativeTypes.containsKey(cls)) {
			return nativeTypes.get(cls);
		} else if (cls.isArray()) {
			ZenType result = new ZenTypeArrayBasic(getNativeType(position, cls.getComponentType(), capture));
			nativeTypes.put(cls, result);
			return result;
		} else {
			ZenTypeNative result = new ZenTypeNative(environment, cls, capture);
			nativeTypes.put(cls, result);
			result.complete(this);
			return result;
		}
	}
	
	private ZenType getListType(ParameterizedType type) {
		if (type.getRawType() == List.class) {
			return new ZenTypeArrayList(getNativeType(null, type.getActualTypeArguments()[0], TypeCapture.EMPTY));
		}
		
		return null;
	}
	
	private ZenType getMapType(ParameterizedType type) {
		if (type.getRawType() == Map.class) {
			return new ZenTypeAssociative(
					getNativeType(null, type.getActualTypeArguments()[1], TypeCapture.EMPTY),
					getNativeType(null, type.getActualTypeArguments()[0], TypeCapture.EMPTY));
		}
		
		return null;
	}
	
	private class StaticGlobalEnvironment implements IScopeMethod {
		@Override
		public TypeRegistry getTypes() {
			return TypeRegistry.this;
		}

		@Override
		public IZenCompileEnvironment getEnvironment() {
			return environment.getEnvironment();
		}

		@Override
		public String makeClassName() {
			return environment.makeClassName();
		}

		@Override
		public boolean containsClass(String name) {
			return environment.containsClass(name);
		}

		@Override
		public Set<String> getClassNames() {
			return environment.getClassNames();
		}

		@Override
		public byte[] getClass(String name) {
			return environment.getClass(name);
		}

		@Override
		public void putClass(String name, byte[] data) {
			environment.putClass(name, data);
		}

		@Override
		public IPartialExpression getValue(String name, ZenPosition position, IScopeMethod environment) {
			return environment.getValue(name, position, environment);
		}

		@Override
		public void putValue(String name, IZenSymbol value, ZenPosition position) {
			throw new UnsupportedOperationException("Not possible!");
		}

		@Override
		public void error(ZenPosition position, String message) {
			environment.error(position, message);
		}

		@Override
		public void warning(ZenPosition position, String message) {
			environment.warning(position, message);
		}

		@Override
		public Statement getControlStatement(String label) {
			return null;
		}

		@Override
		public ZenType getReturnType() {
			return null;
		}
	}
}
