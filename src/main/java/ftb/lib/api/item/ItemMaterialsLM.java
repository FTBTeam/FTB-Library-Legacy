package ftb.lib.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public abstract class ItemMaterialsLM extends ItemLM
{
	public final HashMap<Integer, MaterialItem> materials;
	private String folder = "";
	
	public ItemMaterialsLM()
	{
		materials = new HashMap<>();
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	public void setFolder(String s)
	{ if(s == null || !s.isEmpty()) folder = s; }
	
	public ItemStack add(MaterialItem m)
	{
		materials.put(m.damage, m);
		m.onPostLoaded();
		return m.getStack(1);
	}
	
	public String getUnlocalizedName(ItemStack is)
	{
		MaterialItem m = materials.get(is.getItemDamage());
		if(m != null) return m.getUnlocalizedName();
		return "unknown";
	}
	
	public void onPostLoaded()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs c, List<ItemStack> l)
	{
		for(MaterialItem m : materials.values())
		{
			l.add(m.getStack(1));
		}
	}
	
	public void loadRecipes()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean b)
	{
		MaterialItem m = materials.get(is.getItemDamage());
		if(m != null) m.addInfo(ep, l);
	}
	
	public String getPath(String id, char c)
	{
		if(folder == null) return id;
		return folder + c + id;
	}
}
