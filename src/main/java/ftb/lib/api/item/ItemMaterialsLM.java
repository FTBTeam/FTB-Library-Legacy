package ftb.lib.api.item;

import ftb.lib.api.client.FTBLibClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ItemMaterialsLM extends ItemLM
{
	public final Map<Integer, MaterialItem> materials;
	private String folder;
	
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
		return m.getStack(1);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		MaterialItem m = materials.get(is.getItemDamage());
		if(m != null) return m.getUnlocalizedName();
		return "unknown";
	}
	
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
	public void loadModels()
	{
		for(MaterialItem i : materials.values())
		{
			FTBLibClient.addItemModel(this, i.damage, "variant=" + i.getID());
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs c, List<ItemStack> l)
	{
		for(MaterialItem m : materials.values())
		{
			l.add(m.getStack(1));
		}
	}
	
	@Override
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
