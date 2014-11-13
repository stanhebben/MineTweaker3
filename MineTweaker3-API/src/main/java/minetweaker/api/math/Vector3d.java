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
 * @author Stan Hebben
 */
@ZenClass("minetweaker.util.Vector3d")
public final class Vector3d {
	private final double x;
	private final double y;
	private final double z;
	
	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@ZenGetter("x")
	public double getX() {
		return x;
	}
	
	@ZenGetter("y")
	public double getY() {
		return y;
	}
	
	@ZenGetter("z")
	public double getZ() {
		return z;
	}
}
