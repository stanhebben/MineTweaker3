/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.generic;

import java.util.HashMap;
import java.util.Map;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public class TypeCapture {
	public static final TypeCapture EMPTY = new TypeCapture(null);
	
	private final TypeCapture outer;
	private final Map<ITypeVariable, ZenType> variables;
	
	public TypeCapture(TypeCapture outer) {
		this.outer = outer;
		variables = new HashMap<ITypeVariable, ZenType>();
	}
	
	public ZenType get(ITypeVariable variable) {
		if (variables.containsKey(variable)) {
			return variables.get(variable);
		} else if (outer != null) {
			return outer.get(variable);
		} else {
			throw new RuntimeException("Could not resolve type variable");
		}
	}
	
	public void put(ITypeVariable variable, ZenType type) {
		variables.put(variable, type);
	}
}
