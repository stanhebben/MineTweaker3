/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Stan
 */
public interface IFileLoader {
	public InputStream load(String name) throws IOException;
}
