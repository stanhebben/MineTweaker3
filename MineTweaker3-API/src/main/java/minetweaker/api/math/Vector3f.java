/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.api.math;

import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenGetter;

/**
 *
 * @author Stan
 */
@ZenClass("minetweaker.util.Vector3f")
public class Vector3f {
	private final float x;
	private final float y;
	private final float z;
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@ZenGetter("x")
	public float getX() {
		return x;
	}
	
	@ZenGetter("y")
	public float getY() {
		return y;
	}
	
	@ZenGetter("z")
	public float getZ() {
		return z;
	}
}
