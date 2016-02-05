/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.mc18.game;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.block.IBlockDefinition;
import minetweaker.api.entity.IEntityDefinition;
import minetweaker.api.game.IGame;
import minetweaker.api.item.IItemDefinition;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidDefinition;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.world.IBiome;
// TODO Find this
// import minetweaker.mc1710.GuiCannotRemodify;
import minetweaker.mc18.entity.MCEntityDefinition;
import minetweaker.mc18.item.MCItemDefinition;
import minetweaker.mc18.item.MCItemStack;
import minetweaker.mc18.liquid.MCLiquidDefinition;
import minetweaker.mc18.util.MineTweakerHacks;
import minetweaker.mc18.util.MineTweakerPlatformUtils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

/**
 *
 * @author Stan
 */
public class MCGame implements IGame {
	private static final Map<String, String> TRANSLATIONS = MineTweakerHacks.getTranslations();
	private static final RenderItem RI = Minecraft.getMinecraft().getRenderItem();

	public static final MCGame INSTANCE = new MCGame();

	private boolean locked = false;

	private MCGame() {
	}

	@Override
	public List<IItemDefinition> getItems() {
		List<IItemDefinition> result = new ArrayList<IItemDefinition>();
		for (ResourceLocation res: (Set<ResourceLocation>) Item.itemRegistry.getKeys()) {
			
			result.add(new MCItemDefinition(res.getResourceDomain() + ":" + res.getResourcePath(), (Item) Item.itemRegistry.getObject(res)));
		}
		return result;
	}

	@Override
	public List<IItemStack> getItemStacks() {
		// This code gratuitously stolen from NEI's ItemList.java
		List<IItemStack> result = new ArrayList<IItemStack>();
		List<IItemStack> perms  = new ArrayList<IItemStack>();
		HashSet<Item> bad = new HashSet<Item>();

		for (Item item : (Iterable<Item>) Item.itemRegistry) {
			if (item == null || bad.contains(item)) {
				continue;
			}

			try {
				perms = getPermutations(item);
				if (!perms.isEmpty()) {
					result.addAll(perms);
				}
			} catch (Throwable ex) {
				bad.add(item);
				MineTweakerAPI.getLogger().logError("Failed to get permutations for " + item, ex);
			}
		}
		return result;
	}

	private List<IItemStack> getPermutations(Item item) {
		// This code gratuitously stolen from NEI's ItemList.java
		// NOTE: we don't try to look up NEI's itemOverrides or itemVariants.

		List<IItemStack> perms  = new ArrayList<IItemStack>();
		List<ItemStack> subitems = new ArrayList<ItemStack>();
		item.getSubItems(item, null, subitems);
		if (!subitems.isEmpty()) {
			for (ItemStack stack : subitems) {
				perms.add(new MCItemStack(stack));
			}
			return perms;
		}

		// Search through damage values 0-15 for items with different models.
		HashSet<String> seen = new HashSet<String>();
		for (int damage = 0; damage < 16; damage++) {
			try {
				ItemStack stack = new ItemStack(item, 1, damage);
				IBakedModel model = RI.getItemModelMesher().getItemModel(stack);
				String name = getDisplayName(stack);
				String s = name + "@" + (model == null ? 0 : model.hashCode());
				if (!seen.contains(s)) {
					seen.add(s);
					perms.add(new MCItemStack(stack));
				}
			} catch (Throwable ex) {
				MineTweakerAPI.getLogger().logError("Skipping " + item + ":" + damage, ex);
			}
		}
		return perms;
	}

	private String getDisplayName(ItemStack stack) {
		// This code gratuitously stolen from NEI's GuiContainerManager.java
		List<String> tooltip = null;
		try {
			tooltip = stack.getTooltip(Minecraft.getMinecraft().thePlayer, false);
		} catch (Throwable ignored) {}

		if (tooltip == null || tooltip.size() == 0 || tooltip.get(0) == null || tooltip.get(0) == "") {
			return "Unnamed";
		}
		StringBuilder name = new StringBuilder();
		for (String line : tooltip) {
			// We're only using this for hashset purposes so don't
			// need to bother with appending '#'.
			name.append(line);
		}
		return name.toString();
	}

	@Override
	public List<IBlockDefinition> getBlocks() {
		List<IBlockDefinition> result = new ArrayList<IBlockDefinition>();
		for (String block : (Set<String>) Block.blockRegistry.getKeys()) {
			result.add(MineTweakerMC.getBlockDefinition((Block) Block.blockRegistry.getObject(block)));
		}

		return result;
	}

	@Override
	public List<ILiquidDefinition> getLiquids() {
		List<ILiquidDefinition> result = new ArrayList<ILiquidDefinition>();
		for (Map.Entry<String, Fluid> entry : FluidRegistry.getRegisteredFluids().entrySet()) {
			result.add(new MCLiquidDefinition(entry.getValue()));
		}
		return result;
	}

	@Override
	public List<IBiome> getBiomes() {
		List<IBiome> result = new ArrayList<IBiome>();
		for (IBiome biome : MineTweakerMC.biomes) {
			if (biome != null) {
				result.add(biome);
			}
		}
		return result;
	}

	@Override
	public List<IEntityDefinition> getEntities() {
		List<IEntityDefinition> result = new ArrayList<IEntityDefinition>();

		for (EntityRegistry.EntityRegistration entityRegistration : MineTweakerHacks.getEntityClassRegistrations().values()) {
			result.add(new MCEntityDefinition(entityRegistration));
		}

		return result;
	}

	@Override
	public void setLocalization(String key, String value) {
		MineTweakerAPI.apply(new SetTranslation(null, key, value));
	}

	@Override
	public void setLocalization(String lang, String key, String value) {
		MineTweakerAPI.apply(new SetTranslation(lang, key, value));
	}

	@Override
	public String localize(String key) {
		return LanguageRegistry.instance().getStringLocalization(key);
	}

	@Override
	public String localize(String key, String lang) {
		return LanguageRegistry.instance().getStringLocalization(key, lang);
	}

	@Override
	public void lock()
	{
		locked = true;
	}

	@Override
	public boolean isLocked()
	{
		return locked;
	}

	@Override
	public void signalLockError()
	{
		MineTweakerAPI.getLogger().logError("Reload of scripts blocked (script lock)");

		if (Minecraft.isGuiEnabled()) {
			// Commented out due to unresolved import
			/**
			 * Minecraft.getMinecraft().displayGuiScreen( new GuiCannotRemodify(
			 * "Minecraft has been tweaked for another server",
			 * "with modifications that cannot be rolled back.",
			 * "Please restart your game."));
			 **/
		}
	}

	// ######################
	// ### Action classes ###
	// ######################

	/**
	 * Ported from ModTweaker.
	 * 
	 * @author Joshiejack
	 */
	private static class SetTranslation implements IUndoableAction {
		private String original;
		private final String lang;
		private final String key;
		private final String text;
		private boolean added;

		public SetTranslation(String lang, String key, String text) {
			this.lang = lang;
			this.key = key;
			this.text = text;
		}

		@Override
		public void apply() {
			if (lang == null || MineTweakerPlatformUtils.isLanguageActive(lang)) {
				original = TRANSLATIONS.get(key);
				TRANSLATIONS.put(key, text);
				added = true;
			} else {
				added = false;
			}
		}

		@Override
		public boolean canUndo() {
			return TRANSLATIONS != null;
		}

		@Override
		public void undo() {
			if (added) {
				if (original == null) {
					TRANSLATIONS.remove(key);
				} else {
					TRANSLATIONS.put(key, original);
				}
			}
		}

		@Override
		public String describe() {
			return "Setting localization for the key: " + key + " to " + text;
		}

		@Override
		public String describeUndo() {
			return "Setting localization for the key: " + key + " to " + original;
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
	}
}
