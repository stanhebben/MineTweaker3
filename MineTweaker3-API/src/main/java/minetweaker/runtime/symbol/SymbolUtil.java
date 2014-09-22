/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.symbol;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolJavaStaticField;
import zenscript.runtime.IAny;
import zenscript.symbolic.method.IMethod;
import zenscript.symbolic.method.JavaMethod;

/**
 *
 * @author Stan
 */
public class SymbolUtil {
	public static ITweakerSymbol getStaticMethod(Class cls, String name, Class... argumentTypes) {
		try {
			Method result = cls.getMethod(name, argumentTypes);
			if ((result.getModifiers() & Modifier.STATIC) == 0) {
				return null;
			} else {
				return new TweakerSymbolStaticMethod(result);
			}
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}
	
	public static ITweakerSymbol getStaticField(Class cls, String name) {
		return getStaticField(cls, name, null);
	}
	
	public static ITweakerSymbol getStaticField(Class cls, String name, IAny value) {
		try {
			Field field = cls.getField(name);
			if ((field.getModifiers() & Modifier.STATIC) == 0)
				return null;
			
			return new TweakerSymbolStaticField(field, value);
		} catch (NoSuchFieldException ex) {
			return null;
		}
	}
	
	public static ITweakerSymbol getStaticGetter(Class cls, String name) {
		return getStaticGetter(cls, name, null);
	}
	
	public static ITweakerSymbol getStaticGetter(Class cls, String name, IAny value) {
		try {
			Method result = cls.getMethod(name);
			if ((result.getModifiers() & Modifier.STATIC) == 0) {
				return null;
			} else {
				return new TweakerSymbolStaticGetter(result, value);
			}
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}
	
	public static IMethod getZenStaticMethod(IScopeGlobal scope, Class cls, String name, Class... argumentTypes) {
		return JavaMethod.get(scope.getTypes(), cls, name, argumentTypes);
	}
	
	public static IZenSymbol getZenStaticField(IScopeGlobal scope, Class cls, String name) {
		try {
			Field field = cls.getField(name);
			if ((field.getModifiers() & Modifier.STATIC) == 0)
				return null;
			
			return new SymbolJavaStaticField(field);
		} catch (NoSuchFieldException ex) {
			return null;
		}
	}
}
