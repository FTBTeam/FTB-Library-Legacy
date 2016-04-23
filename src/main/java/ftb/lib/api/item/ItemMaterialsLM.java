package ftb.lib.api.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.HashMap;
import java.util.List;

public abstract class ItemMaterialsLM extends ItemLM
{
	public final HashMap<Integer, MaterialItem> materials;
	public String folder = "";
	private boolean requiresMultipleRenderPasses = false;
	
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
		for(MaterialItem m : materials.values())
			m.onPostLoaded();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs c, List l)
	{
		for(MaterialItem m : materials.values())
			l.add(new ItemStack(item, 1, m.damage));
	}
	
	@Override
	public void loadRecipes()
	{
		for(MaterialItem m : materials.values())
			m.loadRecipes();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer ep, List l, boolean b)
	{
		MaterialItem m = materials.get(is.getItemDamage());
		if(m != null) m.addInfo(ep, l);
	}
	
	@Override
	public int getRenderPasses(int i)
	{
		if(!requiresMultipleRenderPasses) return 1;
		MaterialItem m = materials.get(i);
		if(m != null) return m.getRenderPasses();
		return 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir)
	{
		for(MaterialItem m : materials.values())
			m.registerIcons(ir);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int dmg)
	{
		MaterialItem m = materials.get(dmg);
		if(m != null) return m.getIcon();
		return Items.egg.getIconFromDamage(0);
	}
}
