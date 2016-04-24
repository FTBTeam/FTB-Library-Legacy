package ftb.lib.api.item;

import latmod.lib.IIDObject;
import latmod.lib.LMUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by LatvianModder on 23.04.2016.
 */
public class ItemEntry implements IIDObject
{
	private final String ID;
	public final Item item;
	public final int damage;
	
	public ItemEntry(Item i, int dmg)
	{
		item = i;
		damage = dmg;
		ID = LMInvUtils.getRegName(item) + '@' + damage;
	}
	
	public ItemEntry(ItemStack is)
	{
		this(is.getItem(), is.getItemDamage());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else if(o instanceof ItemEntry)
		{
			ItemEntry e = (ItemEntry) o;
			return (damage == -1 || e.damage == damage) && e.item == item;
		}
		else if(o instanceof ItemStack)
		{
			ItemStack is = (ItemStack) o;
			return (damage == -1 || is.getItemDamage() == damage) && is.getItem() == item;
		}
		
		return LMUtils.getID(o).equals(getID());
	}
	
	@Override
	public String getID()
	{ return ID; }
	
	@Override
	public int hashCode()
	{ return ID.hashCode(); }
}