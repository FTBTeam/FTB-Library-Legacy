package ftb.lib.api.item;

import latmod.lib.util.FinalIDObject;
import net.minecraft.item.ItemStack;

public final class MaterialItem extends FinalIDObject
{
	public ItemMaterialsLM item;
	public final int damage;
	
	public MaterialItem(int d, String s)
	{
		super(s);
		damage = d;
	}
	
	public MaterialItem setItem(ItemMaterialsLM i)
	{
		item = i;
		return this;
	}
	
	public final ItemStack getStack(int s)
	{ return new ItemStack(item, s, damage); }
}