/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode;

import org.openzen.zencode.util.CodePosition;

/**
 * Error logger. Implementations can forward errors to their own error logging
 * system.
 * 
 * @author Stan Hebben
 */
public interface ICodeErrorLogger {
	/**
	 * Checks if any errors have been logged through this logger.
	 * 
	 * @return 
	 */
	public boolean hasErrors();
	
	/**
	 * Called when an error is detected during compilation.
	 * 
	 * @param position error position
	 * @param message error message
	 */
	public void error(CodePosition position, String message);
	
	/**
	 * Called when a warning is generated during compilation.
	 * 
	 * @param position warning position
	 * @param message warning message
	 */
	public void warning(CodePosition position, String message);
}
