package ftb.lib;

import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.block.IBlockLM;
import ftb.lib.api.item.IItemLM;
import ftb.lib.api.recipes.LMRecipes;
import ftb.lib.api.tile.TileLM;
import ftb.lib.mod.*;
import latmod.lib.util.FinalIDObject;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.util.*;

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
			if(FTBLibFinals.DEV) FTBLib.logger.info("LMMod '" + mod.ID + "' created");
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
	public final String assets;
	private ModContainer modContainer;
	public final List<IItemLM> itemsAndBlocks;
	
	public LMRecipes recipes;
	
	public LMMod(String id)
	{
		super(id);
		lowerCaseModID = ID.toLowerCase();
		assets = lowerCaseModID + ":";
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
	
	public CreativeTabs createTab(final String s, final ItemStack icon)
	{
		CreativeTabs tab = new CreativeTabs(assets + s)
		{
			@SideOnly(Side.CLIENT)
			public ItemStack getIconItemStack()
			{ return icon; }
			
			@SideOnly(Side.CLIENT)
			public Item getTabIconItem()
			{ return getIconItemStack().getItem(); }
			
			@SideOnly(Side.CLIENT)
			public void displayAllReleventItems(List l)
			{
				for(IItemLM i : itemsAndBlocks)
				{
					Item item = i.getItem();
					if(item.getCreativeTab() == this) item.getSubItems(item, this, l);
				}
			}
		};
		
		return tab;
	}
	
	public String getBlockName(String s)
	{ return assets + "tile." + s; }
	
	public String getItemName(String s)
	{ return assets + "item." + s; }
	
	public String translate(String s, Object... args)
	{ return FTBLibMod.proxy.translate(assets + s, args); }
	
	public void addItem(IItemLM... ai)
	{
		for(IItemLM i : ai)
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
	}
	
	public IChatComponent chatComponent(String s, Object... obj)
	{ return new ChatComponentTranslation(assets + s, obj); }
}