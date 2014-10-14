/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.type;

import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAssociative;

/**
 *
 * @author Stan
 */
public class ParsedTypeAssociative implements IParsedType {
	private final IParsedType keyType;
	private final IParsedType valueType;
	
	public ParsedTypeAssociative(IParsedType keyType, IParsedType valueType) {
		this.keyType = keyType;
		this.valueType = valueType;
	}

	@Override
	public ZenType compile(IScopeGlobal environment) {
		return new ZenTypeAssociative(
				valueType.compile(environment),
				keyType.compile(environment));
	}
}
