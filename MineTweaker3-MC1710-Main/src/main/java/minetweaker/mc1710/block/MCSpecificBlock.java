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
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Stan
 */
public class MCSpecificBlock implements IBlock {
	private final Block block;
	private final int meta;
	
	public MCSpecificBlock(Block block, int meta) {
		this.block = block;
		this.meta = meta;
	}
	
	@Override
	public IBlockDefinition getDefinition() {
		return MineTweakerMC.getBlockDefinition(block);
	}

	@Override
	public int getMeta() {
		return meta;
	}

	@Override
	public IData getTileData() {
		return null;
	}

	@Override
	public List<IBlock> getPossibleBlocks() {
		return Collections.<IBlock>singletonList(this);
	}

	@Override
	public boolean matches(IBlock block) {
		return block.getDefinition() == getDefinition()
				&& (meta == OreDictionary.WILDCARD_VALUE || block.getMeta() == meta);
	}

	@Override
	public IBlockPattern or(IBlockPattern pattern) {
		return new BlockPatternOr(this, pattern);
	}

	@Override
	public String getDisplayName() {
		return block.getLocalizedName();
	}
	
	@Override
	public String toString() {
		return "<block:" + getBlockId(block) + ":" + (meta == OreDictionary.WILDCARD_VALUE ? '*' : meta) + ">";
	}
	
	private static String getBlockId(Block block)
	{
		return Block.blockRegistry.getNameForObject(block);
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
		return block.getLightOpacity() * 15;
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
}
