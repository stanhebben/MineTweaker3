/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minetweaker;

import minetweaker.api.MineTweakerAPI;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static minetweaker.MineTweakerImplementationAPI.events;
import static minetweaker.MineTweakerImplementationAPI.platform;
import minetweaker.api.block.IBlock;
import minetweaker.api.block.IBlockDefinition;
import minetweaker.api.data.IData;
import minetweaker.api.entity.IEntityDefinition;
import minetweaker.api.event.IEventHandle;
import minetweaker.api.event.PlayerInteractEvent;
import minetweaker.api.item.IItemDefinition;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.WeightedItemStack;
import minetweaker.api.liquid.ILiquidDefinition;
import minetweaker.api.mods.IMod;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.api.player.IPlayer;
import minetweaker.api.recipes.ICraftingRecipe;
import minetweaker.api.server.ICommandFunction;
import minetweaker.api.vanilla.LootEntry;
import minetweaker.api.world.IBiome;
import minetweaker.util.IEventHandler;

/**
 *
 * @author Stan
 */
public class MineTweakerImplementation
{
	private static final Comparator<IItemDefinition> ITEM_COMPARATOR = new ItemComparator();
	private static final Comparator<ILiquidDefinition> LIQUID_COMPARATOR = new LiquidComparator();
	private static final Comparator<IBlockDefinition> BLOCK_COMPARATOR = new BlockComparator();
	private static final Comparator<IEntityDefinition> ENTITY_COMPARATOR = new EntityComparator();

	private static final ListenBlockInfo LISTEN_BLOCK_INFO = new ListenBlockInfo();

	private static final Set<IPlayer> blockInfoPlayers = new HashSet<IPlayer>();
	private static IEventHandle blockEventHandler = null;

	public static void init()
	{
		MineTweakerImplementationAPI.addMineTweakerCommand(
				"reload",
				new String[]{
					"/minetweaker reload",
					"    Reloads all scripts"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						reload();
						player.sendChat("Scripts reloaded");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"names",
				new String[]{
					"/minetweaker names",
					"    Outputs a list of all item names in the game to the minetweaker log"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						List<IItemDefinition> items = MineTweakerAPI.game.getItems();
						Collections.sort(items, ITEM_COMPARATOR);
						for (IItemDefinition item : items) {
							String displayName;

							try {
								displayName = " -- " + item.makeStack(0).getDisplayName();
							} catch (Throwable ex) {
								// some mods (such as buildcraft) may throw exceptions when calling
								// getDisplayName on an item stack that doesn't contain valid NBT data
								// also seems to cause errors in some other cases too
								displayName = " -- Name could not be retrieved due to an error: " + ex;
							}

							MineTweakerAPI.logCommand("<" + item.getId() + ">" + displayName);
						}

						if (player != null)
							player.sendChat("List generated; see minetweaker.log in your minecraft dir");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"liquids",
				new String[]{
					"/minetweaker liquids",
					"    Outputs a list of all liquid names in the game to the minetweaker log"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						List<ILiquidDefinition> liquids = MineTweakerAPI.game.getLiquids();
						Collections.sort(liquids, LIQUID_COMPARATOR);

						MineTweakerAPI.logCommand("Liquids:");
						for (ILiquidDefinition liquid : liquids) {
							MineTweakerAPI.logCommand("<liquid:" + liquid.getName() + "> -- " + liquid.getDisplayName());
						}

						if (player != null)
							player.sendChat("List generated; see minetweaker.log in your minecraft dir");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"blocks",
				new String[]{
					"/minetweaker blocks",
					"    Outputs a list of all blocks in the game to the minetweaker log"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						List<IBlockDefinition> blocks = MineTweakerAPI.game.getBlocks();
						Collections.sort(blocks, BLOCK_COMPARATOR);

						MineTweakerAPI.logCommand("Blocks:");
						for (IBlockDefinition block : blocks) {
							MineTweakerAPI.logCommand("<block:" + block.getId() + "> -- " + block.getDisplayName());
						}

						if (player != null)
							player.sendChat("List generated; see minetweaker.log in your minecraft dir");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"entities",
				new String[]{
					"/minetweaker entities",
					"    Outputs a list of all entity definitions in the game to the minetweaker log"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						List<IEntityDefinition> entities = MineTweakerAPI.game.getEntities();
						Collections.sort(entities, ENTITY_COMPARATOR);

						MineTweakerAPI.logCommand("Entities:");
						for (IEntityDefinition entity : entities) {
							MineTweakerAPI.logCommand(entity.getId() + " -- " + entity.getName());
						}

						if (player != null)
							player.sendChat("List generated; see minetweaker.log in your minecraft dir");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"recipes",
				new String[]{
					"/minetweaker recipes",
					"   Lists all crafting recipes in the game",
					"/minetweaker recipes hand",
					"   Lists all crafting recipes for the item in your hand",
					"   Also copies the recipes to clipboard"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						if (arguments.length == 0) {
							if (player != null)
								player.sendChat("Generating recipe list, this could take a while...");

							MineTweakerAPI.logCommand("Recipes:");
							for (ICraftingRecipe recipe : MineTweakerAPI.recipes.getAll()) {
								MineTweakerAPI.logCommand(recipe.toCommandString());
							}

							if (player != null)
								player.sendChat("Recipe list generated; see minetweaker.log in your minecraft dir");
						} else if (arguments[0].equals("hand") && player != null) {
							IItemStack item = player.getCurrentItem();

							List<ICraftingRecipe> recipes = MineTweakerAPI.recipes.getRecipesFor(item.anyAmount());
							if (recipes.isEmpty())
								player.sendChat("No crafting recipes found for that item");
							else {
								StringBuilder recipesString = new StringBuilder();

								for (ICraftingRecipe recipe : recipes) {
									MineTweakerAPI.logCommand(recipe.toCommandString());
									player.sendChat(recipe.toCommandString());
									recipesString.append(recipe.toCommandString()).append("\n");
								}

								copyToClipboard(recipesString.toString());
							}
						} else
							if (player != null)
								player.sendChat("Invalid arguments for recipes command");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"inventory",
				new String[]{
					"/minetweaker inventory",
					"    Lists all items in your inventory"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						for (int i = 0; i < player.getInventorySize(); i++) {
							IItemStack stack = player.getInventoryStack(i);
							if (stack != null)
								player.sendChat(stack.toString());
						}
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"hand",
				new String[]{
					"/minetweaker hand",
					"    Outputs the name of the item in your hand",
					"    Also copies the name to clipboard and prints",
					"    oredict entries"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						IItemStack hand = player.getCurrentItem();
						if (hand != null) {
							String value = hand.toString();
							player.sendChat(value);
							copyToClipboard(value);

							List<IOreDictEntry> entries = hand.getOres();
							for (IOreDictEntry entry : entries) {
								player.sendChat("Is in <ore:" + entry.getName() + ">");
							}
						}
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"oredict",
				new String[]{
					"/minetweaker oredict",
					"    Outputs all ore dictionary entries in the game to the minetweaker log",
					"/minetweaker oredict <name>",
					"    Outputs all items in the given ore dictionary entry to the minetweaker log"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						if (arguments.length > 0) {
							String entryName = arguments[0];
							IOreDictEntry entry = MineTweakerAPI.oreDict.get(entryName);
							if (entry.isEmpty()) {
								player.sendChat("Entry doesn't exist");
								return;
							} else {
								MineTweakerAPI.logCommand("Ore entries for " + entryName + ":");
								for (IItemStack ore : entry.getItems()) {
									MineTweakerAPI.logCommand("    " + ore);
								}
							}
						} else
							for (IOreDictEntry entry : MineTweakerAPI.oreDict.getEntries()) {
								if (!entry.isEmpty()) {
									MineTweakerAPI.logCommand("Ore entries for <ore:" + entry.getName() + "> :");
									for (IItemStack ore : entry.getItems()) {
										MineTweakerAPI.logCommand("    " + ore);
									}
								}
							}
						player.sendChat("List generated; see minetweaker.log in your minecraft dir");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"mods",
				new String[]{
					"/minetweaker mods",
					"    Outputs all active mod IDs and versions in the game"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						MineTweakerAPI.logCommand("Mods list:");
						for (IMod mod : MineTweakerAPI.loadedMods) {
							String message = mod.getId() + " - " + mod.getName() + " - " + mod.getVersion();
							player.sendChat(message);
							MineTweakerAPI.logCommand("Mod: " + message);
						}
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"name",
				new String[]{
					"/minetweaker name <id>",
					"    Outputs the name for the given item ID",},
				new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						if (arguments.length < 1)
							player.sendChat("missing id parameter");
						else
							try {
								int id = Integer.parseInt(arguments[0]);
								IItemDefinition definition = platform.getItemDefinition(id);
								if (definition == null)
									player.sendChat("no such item");
								else {
									StringBuilder description = new StringBuilder();
									description.append('<');
									description.append(definition.getId());
									description.append('>');
									player.sendChat(description.toString());
								}
							} catch (NumberFormatException e) {
								MineTweakerAPI.logCommand("ID must be an integer");
							}
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"seeds",
				new String[]{
					"/minetweaker seeds",
					"    Prints all seeds registered",
					"    for tall grass"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						MineTweakerAPI.logCommand("Seeds:");
						for (WeightedItemStack seed : MineTweakerAPI.vanilla.getSeeds().getSeeds()) {
							String message = seed.getStack() + " - " + (int) seed.getChance();
							player.sendChat(message);
							MineTweakerAPI.logCommand("Seed: " + message);
						}
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"seeds",
				new String[]{
					"/minetweaker seeds",
					"    Prints all seeds registered",
					"    for tall grass"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						if (arguments.length == 0) {
							MineTweakerAPI.logCommand("Loot chest contents:");
							List<String> types = MineTweakerAPI.vanilla.getLoot().getLootTypes();
							Collections.sort(types);
							for (String lootType : types) {
								MineTweakerAPI.logCommand("Loot type: " + lootType);

								List<LootEntry> entries = MineTweakerAPI.vanilla.getLoot().getLoot(lootType);
								for (LootEntry entry : entries) {
									MineTweakerAPI.logCommand("    " + entry.toString());
								}
							}

							player.sendChat("List generated; see minetweaker.log in your minecraft dir");
						} else {
							MineTweakerAPI.logCommand("Loot for type: " + arguments[0]);

							List<LootEntry> entries = MineTweakerAPI.vanilla.getLoot().getLoot(arguments[0]);
							for (LootEntry entry : entries) {
								MineTweakerAPI.logCommand("    " + entry.toString());
							}

							player.sendChat("List generated; see minetweaker.log in your minecraft dir");
						}
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"wiki",
				new String[]{
					"/minetweaker wiki",
					"    Opens your browser with the wiki"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						player.openBrowser("http://minetweaker3.powerofbytes.com/wiki/");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"bugs",
				new String[]{
					"/minetweaker bugs",
					"    Opens your browser with the GitHub bug tracker"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						player.openBrowser("https://github.com/stanhebben/MineTweaker3/issues");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"forum",
				new String[]{
					"/minetweaker forum",
					"    Opens your browser with the forum"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						player.openBrowser("http://minetweaker3.powerofbytes.com/forum");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"biomes",
				new String[]{
					"/minetweaker biomes",
					"    Lists all the biomes in the game"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						MineTweakerAPI.logCommand("Biomes:");

						for (IBiome biome : MineTweakerAPI.game.getBiomes()) {
							MineTweakerAPI.logCommand("    " + biome.getName());
						}

						player.sendChat("Biome list generated; see minetweaker.log in your minecraft dir");
					}
				});

		MineTweakerImplementationAPI.addMineTweakerCommand(
				"blockinfo",
				new String[]{
					"/minetweaker blockinfo",
					"   Activates or deactivates block reader. In block info mode,",
					"   right-click a block to see ID, meta and tile entity data"
				}, new ICommandFunction()
				{
					@Override
					public void execute(String[] arguments, IPlayer player)
					{
						if (blockInfoPlayers.isEmpty())
							blockEventHandler = events.onPlayerInteract(LISTEN_BLOCK_INFO);

						if (blockInfoPlayers.contains(player)) {
							blockInfoPlayers.remove(player);
							player.sendChat("Block info mode deactivated.");
						} else {
							blockInfoPlayers.add(player);
							player.sendChat("Block info mode activated. Right-click a block to see its data.");
						}

						if (blockInfoPlayers.isEmpty())
							blockEventHandler.close();
					}
				});
	}

	public static void reload()
	{
		blockInfoPlayers.clear();
	}

	private static void copyToClipboard(String value)
	{
		StringSelection stringSelection = new StringSelection(value);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	private static class ItemComparator implements Comparator<IItemDefinition>
	{
		@Override
		public int compare(IItemDefinition o1, IItemDefinition o2)
		{
			return o1.getId().compareTo(o2.getId());
		}
	}

	private static class LiquidComparator implements Comparator<ILiquidDefinition>
	{
		@Override
		public int compare(ILiquidDefinition o1, ILiquidDefinition o2)
		{
			return o1.getName().compareTo(o2.getName());
		}
	}

	private static class BlockComparator implements Comparator<IBlockDefinition>
	{
		@Override
		public int compare(IBlockDefinition o1, IBlockDefinition o2)
		{
			return o1.getId().compareTo(o2.getId());
		}
	}

	private static class EntityComparator implements Comparator<IEntityDefinition>
	{
		@Override
		public int compare(IEntityDefinition o1, IEntityDefinition o2)
		{
			return o1.getId().compareTo(o2.getId());
		}
	}

	private static class ListenBlockInfo implements IEventHandler<PlayerInteractEvent>
	{
		@Override
		public void handle(PlayerInteractEvent event)
		{
			if (blockInfoPlayers.contains(event.getPlayer())) {
				IBlock block = event.getBlock();
				event.getPlayer().sendChat("Block ID: " + block.getDefinition().getId());
				event.getPlayer().sendChat("Meta value: " + block.getMeta());
				IData data = block.getTileData();
				if (data != null)
					event.getPlayer().sendChat("Tile entity data: " + data.asString());
			}
		}
	}
}
