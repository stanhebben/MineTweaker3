/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.elements;

import org.openzen.zencode.parser.type.IParsedType;

/**
 *
 * @author Stan
 */
public class ParsedGenericBoundExtends implements IParsedGenericBound
{
	private final IParsedType type;
	
	public ParsedGenericBoundExtends(IParsedType type)
	{
		this.type = type;
	}
}
