/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.type;

import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ParsedTypeNullable implements IParsedType {
	private final ZenPosition position;
	private final IParsedType baseType;
	
	public ParsedTypeNullable(ZenPosition position, IParsedType baseType) {
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
