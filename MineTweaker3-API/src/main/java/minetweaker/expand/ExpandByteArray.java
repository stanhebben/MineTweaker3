/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.expand;

import minetweaker.api.data.DataByteArray;
import minetweaker.api.data.IData;
import zenscript.annotations.ZenExpansion;

/**
 *
 * @author Stan
 */
@ZenExpansion("byte[]")
public class ExpandByteArray {
	public static IData asData(byte[] data) {
		return new DataByteArray(data, true);
	}
}
