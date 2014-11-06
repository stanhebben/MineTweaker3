/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.TypeRegistry;

/**
 *
 * @author Stan
 */
public enum ParsedTypeBasic implements IParsedType {
	ANY,
	VOID,
	BOOL,
	BYTE,
	SHORT,
	INT,
	LONG,
	FLOAT,
	DOUBLE,
	STRING;

	@Override
	public ZenType compile(IScopeGlobal environment) {
		TypeRegistry types = environment.getTypes();
		
		switch (this) {
			case ANY: return types.ANY;
			case VOID: return types.VOID;
			case BOOL: return types.BOOL;
			case BYTE: return types.BYTE;
			case SHORT: return types.SHORT;
			case INT: return types.INT;
			case LONG: return types.LONG;
			case FLOAT: return types.FLOAT;
			case DOUBLE: return types.DOUBLE;
			case STRING: return types.STRING;
			default:
				throw new AssertionError("Missing enum value: " + this);
		}
	}
}
