package minetweaker.mods.nei;

import codechicken.nei.api.API;
import minetweaker.annotations.ModOnly;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.action.OneWayAction;
import minetweaker.api.action.UndoableAction;
import minetweaker.api.item.IItemStack;
import static minetweaker.api.minecraft.MineTweakerMC.getItemStack;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.annotations.NotNull;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenMethod;

/**
 * MineTweaker NEI support.
 * 
 * Enables hiding NEI items as well as adding new item stacks. These item stacks
 * can then show a custom message or contain NBT data. Can be used to show
 * a custom message or lore with certain items, or to provide spawnable items
 * with specific NBT tags.
 * 
 * @author Stan Hebben
 */
@ZenClass("mods.nei.NEI")
@ModOnly("NotEnoughItems")
public class NEI {
	/**
	 * Hides a specific item in NEI. Will take into account metadata values, if
	 * any.
	 * 
	 * @param item item to be hidden
	 */
	@ZenMethod
	public static void hide(@NotNull IItemStack item) {
		MineTweakerAPI.apply(new NEIHideItemAction(getItemStack(item)));
	}
	
	/**
	 * Adds a NEI entry. The item stack can contain damage values and NBT tags.
	 * 
	 * @param stack item stack to be added
	 */
	@ZenMethod
	public static void addEntry(@NotNull IItemStack stack) {
		MineTweakerAPI.apply(new NEIAddEntryAction(getItemStack(stack)));
	}
	
	/**
	 * Overrides a name in NEI. Note that this will not change the original
	 * display name, but it will change the way it is displayed in NEI.
	 * 
	 * @param item item
	 * @param name item name
	 */
	@ZenMethod
	public static void overrideName(@NotNull IItemStack item, @NotNull String name) {
		MineTweakerAPI.apply(new NEIOverrideNameAction(getItemStack(item), name));
	}
	
	// #############################
	// ### Private inner classes ###
	// #############################
	
	private static class NEIAddEntryAction extends UndoableAction {
		private final ItemStack item;

		public NEIAddEntryAction(ItemStack item) {
			this.item = item;
		}

		@Override
		public void apply() {
			API.addItemListEntry(item);
		}
		
		@Override
		public void undo() {
			API.hideItem(item);
		}

		@Override
		public String describe() {
			return "Adding " + item.getDisplayName() + " as NEI entry";
		}

		@Override
		public String describeUndo() {
			return "Removing " + item.getDisplayName() + " as NEI entry";
		}
	}
	
	private static class NEIHideItemAction extends UndoableAction {
		private final ItemStack stack;

		public NEIHideItemAction(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public void apply() {
			API.hideItem(stack);
		}

		@Override
		public void undo() {
			API.addItemListEntry(stack);
		}

		@Override
		public String describe() {
			return "Hiding " + stack.getUnlocalizedName() + " in NEI";
		}

		@Override
		public String describeUndo() {
			return "Displaying " + stack.getUnlocalizedName() + " in NEI";
		}
	}
	
	private static class NEIOverrideNameAction extends OneWayAction {
		private final ItemStack item;
		private final String name;

		public NEIOverrideNameAction(ItemStack item, String name) {
			this.item = item;
			this.name = name;
		}

		@Override
		public void apply() {
			API.setOverrideName(item, name);
		}

		@Override
		public String describe() {
			return "Overriding NEI item name of " + item.getUnlocalizedName() + " to " + name;
		}
	}
}
