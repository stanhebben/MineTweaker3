/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.type;

import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArrayBasic;

/**
 *
 * @author Stan
 */
public class ParsedTypeArray implements IParsedType {
	private final IParsedType baseType;
	
	public ParsedTypeArray(IParsedType baseType) {
		this.baseType = baseType;
	}

	@Override
	public ZenType compile(IScopeGlobal environment) {
		return new ZenTypeArrayBasic(environment, baseType.compile(environment));
	}
}
