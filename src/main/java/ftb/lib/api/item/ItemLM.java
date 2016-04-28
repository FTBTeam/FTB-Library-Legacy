package ftb.lib.api.item;

import ftb.lib.LMMod;
import ftb.lib.api.client.FTBLibClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemLM extends Item implements IItemLM
{
	public abstract LMMod getMod();
	
	@Override
	public final Item getItem()
	{ return this; }
	
	@Override
	public final String getID()
	{ return getRegistryName().toString(); }
	
	@Override
	public void onPostLoaded()
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void loadModels()
	{
		FTBLibClient.addItemModel(this, 0, "inventory");
	}
	
	@Override
	public void loadRecipes()
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs c, List<ItemStack> l)
	{
		l.add(new ItemStack(item, 1, 0));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{ return getMod().getItemName(getRegistryName().getResourcePath()); }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean b)
	{
	}
}