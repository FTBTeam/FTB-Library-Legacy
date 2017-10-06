package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.lib.util.InvUtils;
import com.feed_the_beast.ftbl.lib.util.misc.Color4I;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ItemIcon extends Icon
{
	private ItemStack stack;
	private String lazyStackString;

	public ItemIcon(ItemStack is)
	{
		stack = is;
		lazyStackString = "";
	}

	public ItemIcon(String s)
	{
		stack = ItemStack.EMPTY;
		lazyStackString = s;
	}

	public ItemStack getStack()
	{
		if (!lazyStackString.isEmpty())
		{
			stack = ItemStackSerializer.parseItem(lazyStackString);
			lazyStackString = "";
		}

		return stack;
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		if (!GuiHelper.drawItem(getStack(), x, y, w / 16D, h / 16D, true, col))
		{
			stack = InvUtils.ERROR_ITEM;
		}
	}

	@Override
	public JsonElement getJson()
	{
		return new JsonPrimitive("item:" + ItemStackSerializer.toString(getStack()));
	}
}