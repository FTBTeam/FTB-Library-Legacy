package ftb.lib;

import cpw.mods.fml.common.*;
import ftb.lib.api.block.IBlockLM;
import ftb.lib.api.item.IItemLM;
import ftb.lib.api.recipes.LMRecipes;
import latmod.lib.util.FinalIDObject;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import java.util.*;

public class LMMod extends FinalIDObject
{
	public static LMMod create(String s)
	{
		LMMod mod = new LMMod(s);
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("LMMod '" + mod.getID() + "' created");
		return mod;
	}
	
	// End of static //
	
	public final String lowerCaseModID;
	private ModContainer modContainer;
	public final List<IItemLM> itemsAndBlocks;
	
	public LMRecipes recipes;
	
	public LMMod(String id)
	{
		super(id);
		lowerCaseModID = id.toLowerCase();
		itemsAndBlocks = new ArrayList<>();
		
		recipes = LMRecipes.defaultInstance;
	}
	
	public ModContainer getModContainer()
	{
		if(modContainer == null) modContainer = Loader.instance().getModObjectList().inverse().get(getID());
		return modContainer;
	}
	
	public void setRecipes(LMRecipes r)
	{ recipes = (r == null) ? new LMRecipes() : r; }
	
	public String toFullID()
	{ return getID() + '-' + Loader.MC_VERSION + '-' + modContainer.getDisplayVersion(); }
	
	public String getBlockName(String s)
	{ return lowerCaseModID + ".tile." + s; }
	
	public String getItemName(String s)
	{ return lowerCaseModID + ".item." + s; }
	
	public void addItem(IItemLM i)
	{
		if(i instanceof IBlockLM)
		{
			FTBLib.addBlock((Block) i, ((IBlockLM) i).getItemBlock(), i.getItemID());
		}
		else
		{
			FTBLib.addItem(i.getItem(), i.getItemID());
		}
		
		itemsAndBlocks.add(i);
	}
	
	public void addTile(Class<? extends TileEntity> c, String s, String... alt)
	{ FTBLib.addTileEntity(c, getID() + '.' + s, alt); }
	
	public void addEntity(Class<? extends Entity> c, String s, int id)
	{ FTBLib.addEntity(c, s, id, getID()); }
	
	public void onPostLoaded()
	{
		for(IItemLM i : itemsAndBlocks) i.onPostLoaded();
	}
	
	public void loadRecipes()
	{
		for(IItemLM i : itemsAndBlocks) i.loadRecipes();
	}
}