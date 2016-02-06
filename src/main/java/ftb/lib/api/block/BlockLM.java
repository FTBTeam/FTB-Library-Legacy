package ftb.lib.api.block;

import ftb.lib.LMMod;
import ftb.lib.api.tile.TileLM;
import ftb.lib.mod.FTBLibMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

public abstract class BlockLM extends BlockContainer implements IBlockLM
{
	public final String blockName;
	
	public BlockLM(String s, Material m)
	{
		super(m);
		blockName = s;
		setUnlocalizedName(getMod().getBlockName(s));
		setHardness(1.8F);
		setResistance(3F);
		isBlockContainer = false;
	}
	
	public Class<? extends ItemBlockLM> getItemBlock()
	{ return ItemBlockLM.class; }
	
	public abstract LMMod getMod();
	
	public abstract TileEntity createNewTileEntity(World w, int m);
	
	public int getRenderType()
	{ return 3; }
	
	public final String getItemID()
	{ return blockName; }
	
	public String getUnlocalizedName()
	{ return getMod().getBlockName(blockName); }
	
	public void onPostLoaded()
	{
		loadModels();
	}
	
	public void loadModels()
	{
		FTBLibMod.proxy.addItemModel(getMod().ID, getItem(), 0, blockName);
	}
	
	public int damageDropped(IBlockState state)
	{ return getMetaFromState(state); }
	
	public boolean hasTileEntity(IBlockState state)
	{ return isBlockContainer; }
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs c, List<ItemStack> l)
	{
		l.add(new ItemStack(item, 1, 0));
	}
	
	public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase el, ItemStack is)
	{
		super.onBlockPlacedBy(w, pos, state, el, is);
		
		if(isBlockContainer && el instanceof EntityPlayer)
		{
			TileLM tile = getTile(w, pos);
			if(tile != null) tile.onPlacedBy((EntityPlayer) el, is, state);
		}
	}
	
	public float getPlayerRelativeBlockHardness(EntityPlayer ep, World w, BlockPos pos)
	{
		if(isBlockContainer)
		{
			TileLM tile = getTile(w, pos);
			if(tile != null && !tile.isMinable(ep)) return -1F;
		}
		
		return super.getPlayerRelativeBlockHardness(ep, w, pos);
	}
	
	public float getBlockHardness(World w, BlockPos pos)
	{
		if(isBlockContainer)
		{
			TileLM tile = getTile(w, pos);
			if(tile != null && !tile.isMinable(null)) return -1F;
		}
		
		return super.getBlockHardness(w, pos);
	}
	
	public float getExplosionResistance(World w, BlockPos pos, Entity e, Explosion ex)
	{
		if(isBlockContainer)
		{
			TileLM tile = getTile(w, pos);
			if(tile != null && tile.isExplosionResistant()) return 1000000F;
		}
		
		return super.getExplosionResistance(w, pos, e, ex);
	}
	
	public int getMobilityFlag()
	{ return isBlockContainer ? 2 : 0; }
	
	public void breakBlock(World w, BlockPos pos, IBlockState state)
	{
		if(!w.isRemote && isBlockContainer)
		{
			TileLM tile = getTile(w, pos);
			if(tile != null) tile.onBroken(state);
		}
		super.breakBlock(w, pos, state);
	}
	
	public boolean onBlockActivated(World w, BlockPos pos, IBlockState state, EntityPlayer ep, EnumFacing s, float x1, float y1, float z1)
	{
		if(!isBlockContainer) return false;
		TileLM tile = getTile(w, pos);
		return (tile != null) ? tile.onRightClick(ep, ep.getHeldItem(), s, x1, y1, z1) : false;
	}
	
	public boolean onBlockEventReceived(World w, BlockPos pos, IBlockState state, int eventID, int param)
	{
		if(isBlockContainer)
		{
			TileLM t = getTile(w, pos);
			if(t != null) return t.receiveClientEvent(eventID, param);
		}
		
		return false;
	}
	
	public boolean recolorBlock(World w, BlockPos pos, EnumFacing side, EnumDyeColor color)
	{
		if(isBlockContainer)
		{
			TileLM t = getTile(w, pos);
			if(t != null)
			{
				if(t.recolourBlock(side, color)) ;
				return true;
			}
		}
		
		return super.recolorBlock(w, pos, side, color);
	}
	
	public void loadRecipes()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean b)
	{
	}
	
	public void onNeighborChange(IBlockAccess w, BlockPos pos, BlockPos neighbor)
	{
		if(isBlockContainer)
		{
			TileLM t = getTile(w, pos);
			if(t != null) t.onNeighborBlockChange(neighbor);
		}
	}
	
	public final Item getItem()
	{ return Item.getItemFromBlock(this); }
	
	public AxisAlignedBB getCollisionBoundingBox(World w, BlockPos pos, IBlockState state)
	{
		setBlockBoundsBasedOnState(w, pos);
		return super.getCollisionBoundingBox(w, pos, state);
	}
	
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{ return getStateFromMeta(meta); }
	
	public TileLM getTile(IBlockAccess w, BlockPos pos)
	{
		TileEntity te = w.getTileEntity(pos);
		if(te != null && !te.isInvalid() && te instanceof TileLM) return ((TileLM) te);
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{ return EnumWorldBlockLayer.SOLID; }
}