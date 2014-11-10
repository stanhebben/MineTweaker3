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
import minetweaker.api.block.IBlock;
import minetweaker.api.block.IBlockDefinition;
import minetweaker.api.block.IBlockPattern;
import minetweaker.api.block.IExplosion;
import minetweaker.api.data.IData;
import minetweaker.api.math.Ray;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import minetweaker.api.render.IRenderer;
import minetweaker.api.world.IBlockGroup;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Stan
 */
public class MCItemBlock implements IBlock {
	private final ItemStack item;
	private final Block block;
	
	public MCItemBlock(ItemStack item) {
		this.item = item;
		this.block = Block.getBlockFromItem(item.getItem());
	}

	@Override
	public IBlockDefinition getDefinition() {
		return MineTweakerMC.getBlockDefinition(block);
	}

	@Override
	public int getMeta() {
		return item.getItemDamage();
	}

	@Override
	public IData getTileData() {
		if (item.stackTagCompound == null)
			return null;
		
		return MineTweakerMC.getIData(item.stackTagCompound);
	}

	@Override
	public String getDisplayName() {
		return item.getDisplayName();
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
	public boolean isAir()
	{
		return false;
	}

	@Override
	public boolean isNormalCube()
	{
		return block.isNormalCube();
	}

	@Override
	public int getBlockLight()
	{
		return block.getLightValue() * 0x11000000 + 0xFFFFFF;
	}
	
	@Override
	public int getBlockOpacity()
	{
		return 15 * block.getLightOpacity();
	}

	@Override
	public float getBlastResistance(IBlockGroup blocks, int x, int y, int z, IExplosion explosion)
	{
		return block.getExplosionResistance(null);
	}

	@Override
	public float getHardness(IBlockGroup blocks, int x, int y, int z, IPlayer player)
	{
		return 0;
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
	public BlockCollision collisionRayTrace(IBlockGroup blocks, int x, int y, int z, Ray ray)
	{
		return null;
	}

	@Override
	public List<IBlock> getPossibleBlocks()
	{
		return Collections.<IBlock>singletonList(this);
	}
}
