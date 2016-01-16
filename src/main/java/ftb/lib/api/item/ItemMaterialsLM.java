package ftb.lib.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

public abstract class ItemMaterialsLM extends ItemLM
{
	public final HashMap<Integer, MaterialItem> materials;
	public String folder = "";
	
	public ItemMaterialsLM(String s)
	{
		super(s);
		materials = new HashMap<>();
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	public ItemStack add(MaterialItem m)
	{
		materials.put(m.damage, m);
		
		if(m.getRenderPasses() > 1) requiresMultipleRenderPasses = true;
		
		return m.getStack();
	}
	
	public String getUnlocalizedName(ItemStack is)
	{
		MaterialItem m = materials.get(is.getItemDamage());
		if(m != null) return m.getUnlocalizedName();
		return "unknown";
	}
	
	public void onPostLoaded()
	{
		for(MaterialItem m : materials.values())
		{
			itemsAdded.add(m.getStack());
			m.onPostLoaded();
		}
	}
	
	public void loadRecipes()
	{
		for(MaterialItem m : materials.values())
			m.loadRecipes();
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean b)
	{
		MaterialItem m = materials.get(is.getItemDamage());
		if(m != null) m.addInfo(ep, l);
	}
	
	public int getRenderPasses(int i)
	{
		if(!requiresMultipleRenderPasses) return 1;
		MaterialItem m = materials.get(i);
		if(m != null) return m.getRenderPasses();
		return 1;
	}
}
