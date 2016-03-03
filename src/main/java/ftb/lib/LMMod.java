package ftb.lib;

import ftb.lib.api.block.IBlockLM;
import ftb.lib.api.item.IItemLM;
import ftb.lib.api.recipes.LMRecipes;
import ftb.lib.api.tile.TileLM;
import ftb.lib.mod.FTBLibMod;
import latmod.lib.util.FinalIDObject;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.*;

import java.util.*;

public class LMMod extends FinalIDObject
{
	private static final HashMap<String, LMMod> modsMap = new HashMap<>();
	
	public static LMMod create(String s)
	{
		if(s == null || s.isEmpty()) return null;
		LMMod mod = modsMap.get(s);
		if(mod == null)
		{
			mod = new LMMod(s);
			modsMap.put(mod.ID, mod);
			if(FTBLib.DEV_ENV) FTBLib.logger.info("LMMod '" + mod.ID + "' created");
		}
		
		return mod;
	}
	
	public static List<LMMod> getMods()
	{
		ArrayList<LMMod> list = new ArrayList<>();
		list.addAll(modsMap.values());
		return list;
	}
	
	// End of static //
	
	public final String lowerCaseModID;
	private ModContainer modContainer;
	public final List<IItemLM> itemsAndBlocks;
	
	public LMRecipes recipes;
	
	public LMMod(String id)
	{
		super(id);
		lowerCaseModID = ID.toLowerCase();
		itemsAndBlocks = new ArrayList<>();
		
		recipes = LMRecipes.defaultInstance;
	}
	
	public ModContainer getModContainer()
	{
		if(modContainer == null) modContainer = Loader.instance().getModObjectList().inverse().get(ID);
		return modContainer;
	}
	
	public void setRecipes(LMRecipes r)
	{ recipes = (r == null) ? new LMRecipes() : r; }
	
	public String toFullID()
	{ return ID + '-' + Loader.MC_VERSION + '-' + modContainer.getDisplayVersion(); }
	
	public String getBlockName(String s)
	{ return lowerCaseModID + ".tile." + s; }
	
	public String getItemName(String s)
	{ return lowerCaseModID + ".item." + s; }
	
	public String translate(String s, Object... args)
	{ return FTBLibMod.proxy.translate(lowerCaseModID + '.' + s, args); }
	
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
	
	public void addTile(Class<? extends TileLM> c, String s, String... alt)
	{ FTBLib.addTileEntity(c, ID + '.' + s, alt); }
	
	public void addEntity(Class<? extends Entity> c, String s, int id)
	{ FTBLib.addEntity(c, s, id, ID); }
	
	public void onPostLoaded()
	{
		for(IItemLM i : itemsAndBlocks) i.onPostLoaded();
	}
	
	public void loadRecipes()
	{
		for(IItemLM i : itemsAndBlocks) i.loadRecipes();
		if(recipes != null) recipes.loadRecipes();
	}
	
	public IChatComponent chatComponent(String s, Object... obj)
	{ return new ChatComponentTranslation(lowerCaseModID + '.' + s, obj); }
}