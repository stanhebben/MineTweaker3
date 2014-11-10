package minetweaker.mods.mfr.machines;

import minetweaker.annotations.ModOnly;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.action.UndoableAction;
import static minetweaker.mc1710.util.MineTweakerPlatformUtils.getLivingEntityClass;
import net.minecraft.entity.EntityLivingBase;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenMethod;
import powercrystals.minefactoryreloaded.MFRRegistry;

/**
 *
 * @author Stan
 */
@ZenClass("mods.mfr.AutoSpawner")
@ModOnly("MineFactoryReloaded")
public class AutoSpawner {
	@ZenMethod
	public static void addBlacklist(String entityClassName) {
		MineTweakerAPI.apply(new AutoSpawnerAddBlacklistAction(getLivingEntityClass(entityClassName)));
	}
	
	@ZenMethod
	public static void removeBlacklist(String entityClassName) {
		MineTweakerAPI.apply(new AutoSpawnerRemoveBlacklistAction(getLivingEntityClass(entityClassName)));
	}
	
	// ######################
	// ### Action classes ###
	// ######################
	
	private static class AutoSpawnerAddBlacklistAction extends UndoableAction {
		private final Class<? extends EntityLivingBase> entityClass;
		private final boolean existed;

		public AutoSpawnerAddBlacklistAction(Class<? extends EntityLivingBase> entityClass) {
			this.entityClass = entityClass;
			existed = MFRRegistry.getAutoSpawnerClassBlacklist().contains(entityClass);
		}

		@Override
		public void apply() {
			if (!existed)
				MFRRegistry.registerAutoSpawnerBlacklistClass(entityClass);
		}

		@Override
		public void undo() {
			if (!existed)
				MFRRegistry.getAutoSpawnerClassBlacklist().remove(entityClass);
		}

		@Override
		public String describe() {
			return "Adding auto-spawner blacklist " + entityClass.getCanonicalName();
		}

		@Override
		public String describeUndo() {
			return "Removing auto-spawner blacklist " + entityClass.getCanonicalName();
		}
	}
	
	private static class AutoSpawnerRemoveBlacklistAction extends UndoableAction {
		private final Class<? extends EntityLivingBase> entityClass;
		private final boolean existed;

		public AutoSpawnerRemoveBlacklistAction(Class<? extends EntityLivingBase> entityClass) {
			this.entityClass = entityClass;
			existed = MFRRegistry.getAutoSpawnerClassBlacklist().contains(entityClass);
		}

		@Override
		public void apply() {
			if (existed)
				MFRRegistry.getAutoSpawnerClassBlacklist().remove(entityClass);
		}

		@Override
		public void undo() {
			if (existed)
				MFRRegistry.getAutoSpawnerClassBlacklist().add(entityClass);
		}

		@Override
		public String describe() {
			return "Removing auto-spawner blacklist " + entityClass.getCanonicalName();
		}

		@Override
		public String describeUndo() {
			return "Restoring auto-spawner blacklist " + entityClass.getCanonicalName();
		}
	}
}
