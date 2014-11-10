/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.mc1710.block;

import java.util.Collections;
import java.util.List;
import minetweaker.api.block.BlockCollision;
import minetweaker.api.block.BlockPatternOr;
import minetweaker.api.block.BlockSide;
import minetweaker.api.block.IBlock;
import minetweaker.api.block.IBlockDefinition;
import minetweaker.api.block.IBlockPattern;
import minetweaker.api.block.IExplosion;
import minetweaker.api.data.IData;
import minetweaker.api.entity.IEntity;
import minetweaker.api.minecraft.MCWorld;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import minetweaker.api.render.IRenderer;
import minetweaker.api.math.Ray;
import minetweaker.api.world.IBlockGroup;
import minetweaker.api.world.IDimension;
import minetweaker.mc1710.entity.MCEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Stan
 */
public class MCWorldBlock implements IBlock {
	private final IBlockAccess blocks;
	private final int x;
	private final int y;
	private final int z;
	private final Block block;
	
	public MCWorldBlock(IBlockAccess blocks, int x, int y, int z) {
		this.blocks = blocks;
		this.x = x;
		this.y = y;
		this.z = z;
		block = blocks.getBlock(x, y, z);
	}

	@Override
	public IBlockDefinition getDefinition() {
		return MineTweakerMC.getBlockDefinition(blocks.getBlock(x, y, z));
	}

	@Override
	public int getMeta() {
		return blocks.getBlockMetadata(x, y, z);
	}

	@Override
	public IData getTileData() {
		TileEntity tileEntity = blocks.getTileEntity(x, y, z);
		
		if (tileEntity == null)
			return null;
		
		NBTTagCompound nbt = new NBTTagCompound();
		tileEntity.writeToNBT(nbt);
		return MineTweakerMC.getIData(nbt);
	}

	@Override
	public String getDisplayName() {
		Block block = blocks.getBlock(x, y, z);
		Item item = Item.getItemFromBlock(block);
		if (item != null) {
			return (new ItemStack(item, 1, getMeta())).getDisplayName();
		} else {
			return block.getLocalizedName();
		}
	}

	@Override
	public List<IBlock> getPossibleBlocks() {
		return Collections.<IBlock>singletonList(this);
	}

	@Override
	public boolean matches(IBlock block) {
		return getDefinition() == block.getDefinition()
				&& (getMeta() == OreDictionary.WILDCARD_VALUE || getMeta() == block.getMeta())
				&& (getTileData() == null || (block.getTileData() != null && block.getTileData().contains(getTileData())));
	}

	@Override
	public IBlockPattern or(IBlockPattern pattern) {
		return new BlockPatternOr(this, pattern);
	}

	@Override
	public boolean isAir() {
		return block.isAir(blocks, x, y, z);
	}

	@Override
	public boolean isNormalCube() {
		return block.isNormalCube(blocks, x, y, z);
	}

	@Override
	public int getBlockLight() {
		return block.getLightValue(blocks, x, y, z);
	}

	@Override
	public float getBlastResistance(IBlockGroup blocks, int x, int y, int z, IExplosion explosion) {
		IDimension dimension = blocks.getDimension();
		Entity mcEntity = MCEntity.getEntity(explosion.getSourceEntity());
		
		if (dimension == blocks) {
			World mcWorld = MCWorld.getWorld(dimension);
			
			return block.getExplosionResistance(mcEntity, mcWorld, x, y, z, explosion.getSourceX(), explosion.getSourceY(), explosion.getSourceZ());
		} else {
			return block.getExplosionResistance(mcEntity);
		}
	}

	@Override
	public float getHardness(IBlockGroup blocks, int x, int y, int z, IPlayer player) {
		IDimension dimension = blocks.getDimension();
		if (dimension == blocks) {
			World mcWorld = MCWorld.getWorld(dimension);
			return block.getBlockHardness(mcWorld, x, y, z);
		} else {
			return block.getBlockHardness(Minecraft.getMinecraft().theWorld, 0, 0, 0);
		}
	}

	@Override
	public void renderWorld(IBlockGroup blocks, int x, int y, int z, IRenderer renderer)
	{
		
	}

	@Override
	public void renderInventory(IRenderer renderer)
	{
		
	}

	@Override
	public BlockCollision collisionRayTrace(IBlockGroup blocks, int x, int y, int z, Ray ray) {
		Vec3 v1 = Vec3.createVectorHelper(ray.getStartX(), ray.getStartY(), ray.getStartZ());
		Vec3 v2 = Vec3.createVectorHelper(ray.getX(1), ray.getY(1), ray.getZ(1));
		
		IDimension dimension = blocks.getDimension();
		if (dimension == blocks) {
			MovingObjectPosition result = block.collisionRayTrace(MCWorld.getWorld(dimension), x, y, z, v1, v2);
			IEntity entity = MCEntity.valueOf(result.entityHit);
			
			IBlock block = blocks.getBlock(result.blockX, result.blockY, result.blockZ);
			BlockSide blockSide = result.sideHit >= 0 && result.sideHit < 6 ? sides[result.sideHit] : null;
			
			double hitx = result.hitVec.xCoord;
			double hity = result.hitVec.yCoord;
			double hitz = result.hitVec.zCoord;
			double distance = ray.nearestDistance(hitx, hity, hitz);
			return new BlockCollision(distance, hitx, hity, hitz, block, blockSide, entity);
		} else {
			return null;
		}
	}
	
	private final BlockSide[] sides = {
		BlockSide.BOTTOM,
		BlockSide.TOP,
		BlockSide.EAST,
		BlockSide.WEST,
		BlockSide.NORTH,
		BlockSide.SOUTH
	};

	@Override
	public int getBlockOpacity()
	{
		return block.getLightOpacity(blocks, x, y, z);
	}
}
