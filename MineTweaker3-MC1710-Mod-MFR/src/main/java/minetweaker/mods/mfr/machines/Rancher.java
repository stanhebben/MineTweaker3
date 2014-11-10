/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.mods.mfr.machines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import minetweaker.annotations.ModOnly;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.action.IUndoableAction;
import minetweaker.api.action.UndoableAction;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.WeightedItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.liquid.WeightedLiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import static minetweaker.mc1710.util.MineTweakerPlatformUtils.getLivingEntityClass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import org.openzen.zencode.annotations.Optional;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenMethod;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.api.IFactoryRanchable;
import powercrystals.minefactoryreloaded.api.RanchedItem;

/**
 *
 * @author Stan
 */
@ZenClass("mods.mfr.AutoSpawner")
@ModOnly("MineFactoryReloaded")
public class Rancher {
	private static final Random random = new Random();
	
	@ZenMethod
	public static void addRanchable(String entityClassName, WeightedItemStack[] possibleDrops, @Optional WeightedLiquidStack[] possibleLiquids) {
		if (possibleLiquids == null)
			possibleLiquids = new WeightedLiquidStack[0];
		
		Class<? extends EntityLivingBase> entityClass = getLivingEntityClass(entityClassName);
		MineTweakerAPI.apply(new AddRanchableAction(new TweakerRanchable(entityClass, possibleDrops, possibleLiquids)));
	}
	
	@ZenMethod
	public static void removeRanchable(String entityClassName) {
		Class<? extends EntityLivingBase> entityClass = getLivingEntityClass(entityClassName);
		if (!MFRRegistry.getRanchables().containsKey(entityClass)) {
			MineTweakerAPI.logWarning("No such ranchable: " + entityClassName);
		} else {
			MineTweakerAPI.apply(new RemoveRanchableAction(MFRRegistry.getRanchables().get(entityClass)));
		}
	}
	
	// #######################
	// ### Private classes ###
	// #######################
	
	private static class TweakerRanchable implements IFactoryRanchable {
		private final Class<? extends EntityLivingBase> entityClass;
		private final WeightedItemStack[] possibleDrops;
		private final WeightedLiquidStack[] possibleLiquids;
		
		public TweakerRanchable(Class<? extends EntityLivingBase> entityClass, WeightedItemStack[] possibleDrops, WeightedLiquidStack[] possibleLiquids) {
			this.entityClass = entityClass;
			this.possibleDrops = possibleDrops;
			this.possibleLiquids = possibleLiquids;
		}
		
		@Override
		public Class<? extends EntityLivingBase> getRanchableEntity() {
			return entityClass;
		}

		@Override
		public List<RanchedItem> ranch(World world, EntityLivingBase entity, IInventory inventory) {
			List<RanchedItem> result = new ArrayList<RanchedItem>();
			if (entityClass.isAssignableFrom(entity.getClass())) {
				for (IItemStack droppedItem : WeightedItemStack.pickRandomDrops(random, possibleDrops)) {
					result.add(new RanchedItem(MineTweakerMC.getItemStack(droppedItem)));
				}
				
				for (ILiquidStack droppedLiquid : WeightedLiquidStack.pickRandomDrops(random, possibleLiquids)) {
					for (int i = 0; i < inventory.getSizeInventory(); i++) {
						ItemStack stack = inventory.getStackInSlot(i);
						if (stack != null) {
							ItemStack filled = FluidContainerRegistry.fillFluidContainer(MineTweakerMC.getLiquidStack(droppedLiquid), stack);
							if (filled != null) {
								inventory.setInventorySlotContents(i, filled);
								break;
							}
						}
					}
				}
			}
			
			return result;
		}
	}
	
	// ######################
	// ### Action classes ###
	// ######################
	
	private static class AddRanchableAction extends UndoableAction {
		private final TweakerRanchable ranchable;
		
		public AddRanchableAction(TweakerRanchable ranchable) {
			this.ranchable = ranchable;
		}

		@Override
		public void apply() {
			MFRRegistry.registerRanchable(ranchable);
		}

		@Override
		public void undo() {
			MFRRegistry.getRanchables().remove(ranchable.entityClass);
		}

		@Override
		public String describe() {
			return "Adding ranchable entity " + ranchable.entityClass.getName();
		}

		@Override
		public String describeUndo() {
			return "Removing ranchable entity " + ranchable.entityClass.getName();
		}
	}
	
	private static class RemoveRanchableAction extends UndoableAction {
		private final IFactoryRanchable ranchable;
		
		public RemoveRanchableAction(IFactoryRanchable ranchable) {
			this.ranchable = ranchable;
		}

		@Override
		public void apply() {
			MFRRegistry.getRanchables().remove(ranchable.getRanchableEntity());
		}
		
		@Override
		public void undo() {
			MFRRegistry.getRanchables().put(ranchable.getRanchableEntity(), ranchable);
		}

		@Override
		public String describe() {
			return "Removing ranchable entity " + ranchable.getRanchableEntity().getName();
		}

		@Override
		public String describeUndo() {
			return "Restoring ranchable entity " + ranchable.getRanchableEntity().getName();
		}
	}
}
