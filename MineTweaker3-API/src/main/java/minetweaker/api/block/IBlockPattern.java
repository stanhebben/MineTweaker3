/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.api.block;

import java.util.List;
import zenscript.annotations.OperatorType;
import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenGetter;
import zenscript.annotations.ZenMethod;
import zenscript.annotations.ZenOperator;

/**
 *
 * @author Stan
 */
@ZenClass("minetweaker.block.IBlockPattern")
public interface IBlockPattern {
	@ZenMethod("blocks")
	public List<IBlock> getBlocks();
	
	@ZenOperator(OperatorType.CONTAINS)
	public boolean matches(IBlock block);
	
	@ZenOperator(OperatorType.OR)
	public IBlockPattern or(IBlockPattern pattern);
	
	@ZenGetter("displayName")
	public String getDisplayName();
	
}
