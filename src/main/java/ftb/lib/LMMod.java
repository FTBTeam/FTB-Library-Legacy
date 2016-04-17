package ftb.lib;

import ftb.lib.api.block.IBlockLM;
import ftb.lib.api.item.IItemLM;
import ftb.lib.api.recipes.LMRecipes;
import ftb.lib.mod.FTBLibMod;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public class LMMod extends FinalIDObject
{
	public static LMMod create(String s)
	{
		LMMod mod = new LMMod(s);
		if(FTBLib.DEV_ENV) FTBLibMod.logger.info("LMMod '" + mod.getID() + "' created");
		return mod;
	}
	
	public final String lowerCaseModID;
	private final String modAssets;
	private ModContainer modContainer;
	public final List<IItemLM> itemsAndBlocks;
	public LMRecipes recipes;
	
	public LMMod(String id)
	{
		super(id);
		lowerCaseModID = getID().toLowerCase();
		modAssets = lowerCaseModID + '.';
		itemsAndBlocks = new ArrayList<>();
		recipes = LMRecipes.defaultInstance;
	}
	
	public ModContainer getModContainer()
	{
		if(modContainer == null)
		{
			modContainer = Loader.instance().getModObjectList().inverse().get(getID());
		}
		
		return modContainer;
	}
	
	public void setRecipes(LMRecipes r)
	{ recipes = (r == null) ? new LMRecipes() : r; }
	
	public String getFullID()
	{ return getID() + '-' + Loader.MC_VERSION + '-' + modContainer.getDisplayVersion(); }
	
	public String getBlockName(String s)
	{ return modAssets + "tile." + s; }
	
	public String getItemName(String s)
	{ return modAssets + "item." + s; }
	
	public <K extends IForgeRegistryEntry<?>> K register(String s, K object)
	{
		object.setRegistryName(new ResourceLocation(getID(), s));
		GameRegistry.register(object);
		
		if(object instanceof IItemLM)
		{
			itemsAndBlocks.add((IItemLM) object);
			
			if(object instanceof IBlockLM)
			{
				ItemBlock ib = ((IBlockLM) object).createItemBlock();
				ib.setRegistryName(new ResourceLocation(getID(), s));
				GameRegistry.register(ib);
			}
		}
		
		return object;
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
		if(recipes != null) recipes.loadRecipes();
	}
	
	@SideOnly(Side.CLIENT)
	@Deprecated
	public String format(String s, Object... args)
	{ return I18n.format(modAssets + s, args); }
	
	@Deprecated
	public ITextComponent chatComponent(String s, Object... obj)
	{ return new TextComponentTranslation(modAssets + s, obj); }
}