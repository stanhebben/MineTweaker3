/*
 * Part of MineTweaker3 API - MIT license applies.
 */
package minetweaker.runtime;

import minetweaker.api.MineTweakerAPI;

/**
 * Globally available functions. Made available through global variables.
 *
 * API Status: existing methods will not be modified.
 *
 * @author Stan Hebben
 */
public class GlobalFunctions
{
	private GlobalFunctions() {}

	public static void print(String message)
	{
		MineTweakerAPI.logInfo(message);
	}
}
