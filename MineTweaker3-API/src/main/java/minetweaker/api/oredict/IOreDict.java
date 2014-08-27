/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.api.oredict;

import java.util.List;
import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenGetter;
import zenscript.annotations.ZenMemberGetter;

/**
 *
 * @author Stan
 */
@ZenClass("minetweaker.oredict.IOreDict")
public interface IOreDict {
	@ZenMemberGetter
	public IOreDictEntry get(String name);
	
	@ZenGetter("entries")
	public List<IOreDictEntry> getEntries();
}
