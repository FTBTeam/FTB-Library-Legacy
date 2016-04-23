package ftb.lib;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.item.IItemLM;
import latmod.lib.LMUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 06.02.2016.
 */
public class CreativeTabLM extends CreativeTabs
{
	private LMMod mod;
	private final List<ItemStack> iconItems;
	private long timer = 1000L;
	
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
	
	public CreativeTabLM setTimer(long l)
	{
		timer = Math.max(l, 100L);
		return this;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack()
	{
		if(!iconItems.isEmpty())
		{
			if(iconItems.size() == 1) return iconItems.get(0);
			return iconItems.get((int) ((LMUtils.millis() / timer) % iconItems.size()));
		}
		
		return new ItemStack(Items.diamond);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{ return getIconItemStack().getItem(); }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int func_151243_f()
	{ return getIconItemStack().getItemDamage(); }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllReleventItems(List l)
	{
		if(mod == null) super.displayAllReleventItems(l);
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
