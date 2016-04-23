package ftb.lib.api.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.LMMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ItemLM extends Item implements IItemLM
{
	public final String itemName;
	
	public ItemLM(String s)
	{
		super();
		itemName = s;
		setUnlocalizedName(getMod().getItemName(s));
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	public abstract LMMod getMod();
	
	@Override
	public final Item getItem()
	{ return this; }
	
	@Override
	public final String getItemID()
	{ return itemName; }
	
	@Override
	public void onPostLoaded()
	{
	}
	
	@Override
	public void loadRecipes()
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs c, List l)
	{
		l.add(new ItemStack(item, 1, 0));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected String getIconString()
	{ return getMod().lowerCaseModID + ":" + itemName; }
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{ return getMod().getItemName(itemName); }
	
	@Override
	public int getRenderPasses(int m)
	{ return 1; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer ep, List l, boolean b)
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir)
	{ itemIcon = ir.registerIcon(getIconString()); }
}