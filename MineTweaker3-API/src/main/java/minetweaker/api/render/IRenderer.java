/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.render;

/**
 * Renderers provide an interface to the underlying rendering mechanism, such
 * as OpenGL. They are used to render blocks, items and any other kind of models.
 * 
 * @author Stan Hebben
 */
public interface IRenderer {
	public void addTranslation(double x, double y, double z);
}
