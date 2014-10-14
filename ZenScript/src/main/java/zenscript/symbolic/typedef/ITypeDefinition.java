/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.typedef;

import java.util.List;
import org.objectweb.asm.ClassVisitor;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.method.IMethod;

/**
 * Compilable type definition. Can be directly compiled to a Java class.
 * @author Stan
 */
public interface ITypeDefinition {
	public void addMethod(IMethod method);
	
	/**
	 * Adds a value map to the type definition. Will create a hashmap field mapping
	 * the given values to integers being the list's indices.
	 * 
	 * Used by switch statements.
	 * 
	 * @param type value type
	 * @param values key values
	 * @return field name
	 */
	public String addSwitchValueMap(ZenType type, List<Expression> values);
	
	/**
	 * Compiles this type definition.
	 * 
	 * @param cls class to compile to
	 */
	public void compile(ClassVisitor cls);
}
