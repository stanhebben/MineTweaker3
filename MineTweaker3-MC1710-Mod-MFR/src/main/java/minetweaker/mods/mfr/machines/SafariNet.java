/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class SafariNet {
	@ZenMethod
	public static void addBlacklist(String entityClassName) {
		Class<? extends EntityLivingBase> entityClass = getLivingEntityClass(entityClassName);
		MineTweakerAPI.apply(new AddBlacklistAction(entityClass));
	}
	
	@ZenMethod
	public static void removeBlacklist(String entityClassName) {
		Class<? extends EntityLivingBase> entityClass = getLivingEntityClass(entityClassName);
		if (!MFRRegistry.getSafariNetBlacklist().contains(entityClass)) {
			MineTweakerAPI.logWarning(entityClassName + " is not in the safari net blacklist");
		} else {
			MineTweakerAPI.apply(new RemoveBlacklistAction(entityClass));
		}
	}
	
	// ######################
	// ### Action classes ###
	// ######################
	
	private static class AddBlacklistAction extends UndoableAction {
		private final Class<? extends EntityLivingBase> entityClass;
		
		public AddBlacklistAction(Class<? extends EntityLivingBase> entityClass) {
			this.entityClass = entityClass;
		}

		@Override
		public void apply() {
			MFRRegistry.registerSafariNetBlacklist(entityClass);
		}

		@Override
		public void undo() {
			MFRRegistry.getSafariNetBlacklist().remove(entityClass);
		}

		@Override
		public String describe() {
			return "Blacklisting " + entityClass.getName() + " in the safari net";
		}

		@Override
		public String describeUndo() {
			return "Removing " + entityClass.getName() + " from the safari net blacklist";
		}
	}
	
	private static class RemoveBlacklistAction extends UndoableAction {
		private final Class<? extends EntityLivingBase> entityClass;
		
		public RemoveBlacklistAction(Class<? extends EntityLivingBase> entityClass) {
			this.entityClass = entityClass;
		}

		@Override
		public void apply() {
			MFRRegistry.getSafariNetBlacklist().remove(entityClass);
		}

		@Override
		public void undo() {
			MFRRegistry.getSafariNetBlacklist().add(entityClass);
		}

		@Override
		public String describe() {
			return "Removing " + entityClass.getName() + " from the safari net blacklist";
		}

		@Override
		public String describeUndo() {
			return "Restoring " + entityClass.getName() + " to the safari net blacklist";
		}
	}
}
