package ftb.lib.api.item;

import ftb.lib.LMMod;
import ftb.lib.mod.FTBLibMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

public abstract class ItemLM extends Item implements IItemLM
{
	public final String itemName;
	
	public ItemLM(String s)
	{
		super();
		itemName = s;
		setUnlocalizedName(getMod().getItemName(s));
	}
	
	public abstract LMMod getMod();
	
	public final Item getItem()
	{ return this; }
	
	public final String getID()
	{ return itemName; }
	
	public void onPostLoaded()
	{
		loadModels();
	}
	
	public void loadModels()
	{
		FTBLibMod.proxy.addItemModel(getMod().getID(), getItem(), 0, itemName);
	}
	
	public void loadRecipes()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs c, List<ItemStack> l)
	{
		l.add(new ItemStack(item, 1, 0));
	}
	
	public String getUnlocalizedName(ItemStack is)
	{ return getMod().getItemName(itemName); }
	
	public int getRenderPasses(int m)
	{ return 1; }
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean b)
	{
	}
}