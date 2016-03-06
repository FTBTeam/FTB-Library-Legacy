package ftb.lib.api.item;

import ftb.lib.mod.FTBLibMod;
import latmod.lib.util.FinalIDObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.*;

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
	
	public final ItemStack getStack(int s)
	{ return new ItemStack(item, s, damage); }
	
	public void onPostLoaded()
	{
		FTBLibMod.proxy.addItemModel(item.getMod().getID(), item, damage, item.getID(), "variant=" + getID());
	}
	
	public int getRenderPasses()
	{ return 1; }
	
	@SideOnly(Side.CLIENT)
	public void addInfo(EntityPlayer ep, List<String> l)
	{
	}
	
	public String getUnlocalizedName()
	{ return item.getMod().getItemName(item.getPath(getID(), '.')); }
}