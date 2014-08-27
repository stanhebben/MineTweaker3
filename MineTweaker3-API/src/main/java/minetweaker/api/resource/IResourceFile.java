/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.api.resource;

import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenGetter;

/**
 *
 * @author Stan
 */
@ZenClass("minetweaker.resource.IResourceFile")
public interface IResourceFile {
	@ZenGetter("name")
	public String getName();
}
