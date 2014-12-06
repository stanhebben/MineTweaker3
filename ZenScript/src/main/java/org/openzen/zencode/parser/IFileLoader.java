/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Stan
 */
public interface IFileLoader
{
	public InputStream load(String name) throws IOException;
}
