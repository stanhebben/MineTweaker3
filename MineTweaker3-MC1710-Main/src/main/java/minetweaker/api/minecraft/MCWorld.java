/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.minecraft;

import java.util.HashMap;
import minetweaker.api.world.IDimension;
import minetweaker.mc1710.world.MCDimension;
import net.minecraft.world.World;

/**
 *
 * @author Stan
 */
public class MCWorld {
	private MCWorld() {}
	
	private static final HashMap<World, IDimension> dimensions = new HashMap<World, IDimension>();
	
	public static IDimension getDimension(World world) {
		if (!dimensions.containsKey(world)) {
			dimensions.put(world, new MCDimension(world));
		}
		
		return dimensions.get(world);
	}
	
	public static World getWorld(IDimension dimension) {
		if (dimension instanceof MCDimension) {
			return ((MCDimension) dimension).getWorld();
		} else {
			return null;
		}
	}
}
