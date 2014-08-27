/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.expand;

import minetweaker.api.data.DataIntArray;
import minetweaker.api.data.IData;
import zenscript.annotations.ZenExpansion;

/**
 *
 * @author Stan
 */
@ZenExpansion("int[]")
public class ExpandIntArray {
	public static IData asData(int[] data) {
		return new DataIntArray(data, true);
	}
}
