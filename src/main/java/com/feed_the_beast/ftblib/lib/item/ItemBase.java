package com.feed_the_beast.ftblib.lib.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author LatvianModder
 */
public class ItemBase extends Item
{
	public ItemBase(String mod, String id)
	{
		setRegistryName(mod, id);
		setUnlocalizedName(mod + '.' + id);
		setCreativeTab(CreativeTabs.MISC);
	}
}