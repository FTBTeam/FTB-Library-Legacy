package ftb.lib.api.item;

import latmod.lib.LMUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
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
	{ if(s == null || !s.isEmpty()) { folder = s; } }
	
	public final void add(MaterialItem m)
	{
		m.setItem(this);
		materials.put(m.damage, m);
	}
	
	public final void addAll(Class<?> c)
	{
		try
		{
			for(MaterialItem m : LMUtils.getObjects(MaterialItem.class, c, null))
			{
				add(m);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		MaterialItem m = materials.get(is.getItemDamage());
		
		if(m != null)
		{
			return getMod().getItemName(getPath(m.getID(), '.'));
		}
		
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
			ModelLoader.setCustomModelResourceLocation(this, i.damage, new ModelResourceLocation(getRegistryName(), "variant=" + i.getID()));
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
	
	public String getPath(String id, char c)
	{
		if(folder == null) { return id; }
		return folder + c + id;
	}
}
