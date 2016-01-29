package ftb.lib.api.item;

import cpw.mods.fml.relauncher.*;
import latmod.lib.util.FinalIDObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MaterialItem extends FinalIDObject
{
	public final ItemMaterialsLM item;
	public final int damage;
	
	public MaterialItem(ItemMaterialsLM i, int d, String s)
	{
		super(s);
		item = i;
		damage = d;
	}
	
	public ItemStack getStack(int s)
	{ return new ItemStack(item, s, damage); }
	
	public ItemStack getStack()
	{ return getStack(1); }
	
	public void onPostLoaded() { }
	
	public void loadRecipes() { }
	
	public int getRenderPasses()
	{ return 1; }
	
	@SideOnly(Side.CLIENT)
	public void addInfo(EntityPlayer ep, List<String> l)
	{ }
	
	public String getUnlocalizedName()
	{ return item.getMod().getItemName(item.folder.isEmpty() ? ID : (item.folder + "." + ID)); }
}