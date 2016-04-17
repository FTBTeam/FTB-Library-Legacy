package ftb.lib;

import ftb.lib.api.item.IItemLM;
import latmod.lib.LMUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 06.02.2016.
 */
public class CreativeTabLM extends CreativeTabs
{
	private LMMod mod;
	private final List<ItemStack> iconItems;
	
	public CreativeTabLM(String label)
	{
		super(label);
		iconItems = new ArrayList<>();
	}
	
	public CreativeTabLM setMod(LMMod m)
	{
		mod = m;
		return this;
	}
	
	public CreativeTabLM addIcon(ItemStack is)
	{
		if(is != null) iconItems.add(is);
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack()
	{
		if(!iconItems.isEmpty())
		{
			if(iconItems.size() == 1) return iconItems.get(0);
			return iconItems.get((int) ((LMUtils.millis() / 1000L) % iconItems.size()));
		}
		
		return new ItemStack(Items.diamond);
	}
	
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{ return getIconItemStack().getItem(); }
	
	@SideOnly(Side.CLIENT)
	public int getIconItemDamage()
	{ return getIconItemStack().getItemDamage(); }
	
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(List<ItemStack> l)
	{
		if(mod == null) super.displayAllRelevantItems(l);
		else
		{
			for(IItemLM i : mod.itemsAndBlocks)
			{
				Item item = i.getItem();
				if(item.getCreativeTab() == this) item.getSubItems(item, this, l);
			}
		}
	}
}
