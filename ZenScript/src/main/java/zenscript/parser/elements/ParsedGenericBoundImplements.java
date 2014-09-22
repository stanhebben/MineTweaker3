/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.elements;

import zenscript.parser.type.IParsedType;

/**
 *
 * @author Stan
 */
public class ParsedGenericBoundImplements implements IParsedGenericBound {
	private final IParsedType type;
	
	public ParsedGenericBoundImplements(IParsedType type) {
		this.type = type;
	}
}
