/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.api.recipes;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import zenscript.annotations.Optional;
import zenscript.annotations.ZenClass;
import zenscript.annotations.ZenMethod;

/**
 *
 * @author Stan
 */
@ZenClass("minetweaker.recipes.IFurnaceManager")
public interface IFurnaceManager {
	@ZenMethod
	public void remove(IIngredient output, @Optional IIngredient input);
	
	@ZenMethod
	public void addRecipe(IItemStack output, IIngredient input, @Optional double xp);
	
	@ZenMethod
	public void setFuel(IIngredient item, int fuel);
	
	@ZenMethod
	public int getFuel(IItemStack item);
}
