/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedTypeNullable implements IParsedType {
	private final CodePosition position;
	private final IParsedType baseType;
	
	public ParsedTypeNullable(CodePosition position, IParsedType baseType) {
		this.position = position;
		this.baseType = baseType;
	}

	@Override
	public ZenType compile(IScopeGlobal environment) {
		ZenType cBaseType = baseType.compile(environment);
		ZenType type = cBaseType.nullable();
		if (type == null) {
			environment.error(position, baseType + " is not a nullable type");
			type = environment.getTypes().ANY;
		}
		return type;
	}
}
