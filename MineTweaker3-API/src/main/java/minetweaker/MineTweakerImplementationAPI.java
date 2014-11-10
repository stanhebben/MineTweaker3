package minetweaker;

import minetweaker.api.action.IUndoableAction;
import minetweaker.api.IPlatformFunctions;
import minetweaker.api.MineTweakerAPI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static minetweaker.api.MineTweakerAPI.server;
import minetweaker.api.event.IEventHandle;
import minetweaker.api.event.MTEventManager;
import minetweaker.api.event.PlayerLoggedInEvent;
import minetweaker.api.event.PlayerLoggedOutEvent;
import minetweaker.api.event.ReloadEvent;
import minetweaker.api.formatting.IFormatter;
import minetweaker.api.game.IGame;
import minetweaker.api.logger.MTLogger;
import minetweaker.api.mods.ILoadedMods;
import minetweaker.api.oredict.IOreDict;
import minetweaker.api.player.IPlayer;
import minetweaker.api.recipes.IFurnaceManager;
import minetweaker.api.recipes.IRecipeManager;
import minetweaker.api.server.ICommandFunction;
import minetweaker.api.server.ICommandValidator;
import minetweaker.api.server.IServer;
import minetweaker.api.vanilla.IVanilla;
import minetweaker.runtime.IScriptProvider;
import minetweaker.runtime.Tweaker;
import minetweaker.util.EventList;
import minetweaker.util.IEventHandler;

/**
 * The implementation API is used by API implementations for internal
 * communication and initialization.
 *
 * @author Stan Hebben
 */
public class MineTweakerImplementationAPI
{
	private static final Map<String, MineTweakerCommand> minetweakerCommands;

	private static final ListenPlayerLoggedIn LISTEN_LOGIN = new ListenPlayerLoggedIn();
	private static final ListenPlayerLoggedOut LISTEN_LOGOUT = new ListenPlayerLoggedOut();

	private static final EventList<ReloadEvent> ONPRELOAD = new EventList<ReloadEvent>();
	private static final EventList<ReloadEvent> ONPOSTLOAD = new EventList<ReloadEvent>();

	private static final Tweaker tweaker = new Tweaker();

	static {
		minetweakerCommands = new HashMap<String, MineTweakerCommand>();
		MineTweakerImplementation.init();
	}

	public static Tweaker getTweaker()
	{
		return tweaker;
	}

	/**
	 * Access point to the event handler implementation.
	 */
	public static final MTEventManager events = new MTEventManager();

	/**
	 * Access point to the internal logger instance.
	 */
	public static final MTLogger logger = new MTLogger();

	/**
	 * Access point to general platform functions.
	 */
	public static IPlatformFunctions platform = null;

	/**
	 * Initializes the MineTweaker API.
	 *
	 * @param oreDict ore dictionary interface
	 * @param recipes recipe manager interface
	 * @param furnace furnace manager interface
	 * @param game game interface
	 * @param mods mods interface
	 * @param formatter formatter interface
	 * @param vanilla vanilla interface
	 */
	public static void init(
			IOreDict oreDict,
			IRecipeManager recipes,
			IFurnaceManager furnace,
			IGame game,
			ILoadedMods mods,
			IFormatter formatter,
			IVanilla vanilla)
	{
		MineTweakerAPI.oreDict = oreDict;
		MineTweakerAPI.recipes = recipes;
		MineTweakerAPI.furnace = furnace;
		MineTweakerAPI.game = game;
		MineTweakerAPI.loadedMods = mods;
		MineTweakerAPI.format = formatter;
		MineTweakerAPI.vanilla = vanilla;
	}

	/**
	 * Register an event handler to be fired upon reload.
	 *
	 * @param handler
	 * @return
	 */
	public static IEventHandle onPreLoad(IEventHandler<ReloadEvent> handler)
	{
		return ONPRELOAD.add(handler);
	}

	public static IEventHandle onPostLoad(IEventHandler<ReloadEvent> handler)
	{
		return ONPOSTLOAD.add(handler);
	}

	/**
	 * Must be called upon server start.
	 *
	 * @param server server interface
	 */
	public static void onServerStart(IServer server)
	{
		MineTweakerAPI.server = server;
		reload();
	}

	/**
	 * Must be called upon server stop.
	 */
	public static void onServerStop()
	{
		MineTweakerAPI.server = null;
	}

	/**
	 * Sets the script provider.
	 *
	 * @param provider script provider
	 */
	public static void setScriptProvider(IScriptProvider provider)
	{
		tweaker.setScriptProvider(provider);
	}

	/**
	 * Called to reload scripts. Must be called after setting a new script
	 * provider in order to reload scripts.
	 */
	public static void reload()
	{
		MineTweakerImplementation.reload();

		logger.clear();
		events.clear();

		if (MineTweakerAPI.server != null)
		{
			events.onPlayerLoggedIn(LISTEN_LOGIN);
			events.onPlayerLoggedOut(LISTEN_LOGOUT);
		}

		tweaker.rollback();

		if (MineTweakerAPI.server != null)
			initServer();

		ONPRELOAD.publish(new ReloadEvent());

		tweaker.load();

		ONPOSTLOAD.publish(new ReloadEvent());

		if (MineTweakerAPI.server != null)
			platform.distributeScripts(tweaker.getScriptData());
	}
	
	private static void initServer()
	{
		server.addCommand("minetweaker", "", new String[]{"mt"}, new ICommandFunction()
		{
			@Override
			public void execute(String[] arguments, IPlayer player)
			{
				if (arguments.length == 0)
					player.sendChat("Please provide a command. Use /mt help for more info.");
				else if (arguments[0].equals("help")) {
					String[] keys = minetweakerCommands.keySet().toArray(new String[minetweakerCommands.size()]);
					Arrays.sort(keys);
					for (String key : keys) {
						for (String helpMessage : minetweakerCommands.get(key).description) {
							player.sendChat(helpMessage);
						}
					}
				} else {
					MineTweakerCommand command = minetweakerCommands.get(arguments[0]);
					if (command == null)
						player.sendChat("No such minetweaker command available");
					else
						command.function.execute(Arrays.copyOfRange(arguments, 1, arguments.length), player);
				}
			}
		}, new ICommandValidator()
		{
			@Override
			public boolean canExecute(IPlayer player)
			{
				return server.isOp(player);
			}
		}, null, true);
	}

	/**
	 * Adds a new minetweaker command. Can be called with /mt &lt;command&;gt;
	 * &lt;arguments&gt;.
	 *
	 * @param name command name
	 * @param description description strings
	 * @param function command implementation
	 */
	public static void addMineTweakerCommand(String name, String[] description, ICommandFunction function)
	{
		MineTweakerAPI.apply(new AddMineTweakerCommandAction(new MineTweakerCommand(name, description, function)));
	}

	// ######################
	// ### Action classes ###
	// ######################
	private static class AddMineTweakerCommandAction implements IUndoableAction
	{
		private final MineTweakerCommand command;
		private boolean added;

		public AddMineTweakerCommandAction(MineTweakerCommand command)
		{
			this.command = command;
		}

		@Override
		public void apply()
		{
			if (!minetweakerCommands.containsKey(command.name)) {
				minetweakerCommands.put(command.name, command);
				added = true;
			} else
				added = false;
		}

		@Override
		public boolean canUndo()
		{
			return true;
		}

		@Override
		public void undo()
		{
			if (added)
				minetweakerCommands.remove(command.name);
		}

		@Override
		public String describe()
		{
			return "Adding minetweaker command " + command.name;
		}

		@Override
		public String describeUndo()
		{
			return "Removing minetweaker command " + command.name;
		}

		@Override
		public boolean isSilent()
		{
			return true;
		}
	}

	// #############################
	// ### Private inner classes ###
	// #############################
	private static class MineTweakerCommand
	{
		private final String name;
		private final String[] description;
		private final ICommandFunction function;

		public MineTweakerCommand(String name, String[] description, ICommandFunction function)
		{
			this.name = name;
			this.description = description;
			this.function = function;
		}
	}

	private static class ListenPlayerLoggedIn implements IEventHandler<PlayerLoggedInEvent>
	{
		@Override
		public void handle(PlayerLoggedInEvent event)
		{
			if (MineTweakerAPI.server != null && MineTweakerAPI.server.isOp(event.getPlayer()))
				logger.addPlayer(event.getPlayer());
		}
	}

	private static class ListenPlayerLoggedOut implements IEventHandler<PlayerLoggedOutEvent>
	{
		@Override
		public void handle(PlayerLoggedOutEvent event)
		{
			logger.removePlayer(event.getPlayer());
		}
	}
}
