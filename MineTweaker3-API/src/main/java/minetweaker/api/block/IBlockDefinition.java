package minetweaker.api.block;

import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenGetter;

/**
 * Blocks definitions provide additional information about blocks.
 * 
 * @author Stan Hebben
 */
@ZenClass("minetweaker.block.IBlockDefinition")
public interface IBlockDefinition {
	@ZenGetter("id")
	public String getId();
	
	@ZenGetter("displayName")
	public String getDisplayName();
}
