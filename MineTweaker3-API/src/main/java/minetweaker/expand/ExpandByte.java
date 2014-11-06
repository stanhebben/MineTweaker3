/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.expand;

import minetweaker.api.data.DataByte;
import minetweaker.api.data.IData;
import org.openzen.zencode.annotations.ZenCaster;
import org.openzen.zencode.annotations.ZenExpansion;

/**
 *
 * @author Stanneke
 */
@ZenExpansion("byte")
public class ExpandByte {
	@ZenCaster
	public static IData asData(byte value) {
		return new DataByte(value);
	}
}
