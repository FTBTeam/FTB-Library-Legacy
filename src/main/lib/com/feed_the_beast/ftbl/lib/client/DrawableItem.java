package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.lib.util.InvUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class DrawableItem implements IDrawableObject
{
	private ItemStack stack;
	private String lazyStackString;

	public DrawableItem(ItemStack is)
	{
		stack = is;
		lazyStackString = "";
	}

	public DrawableItem(String s)
	{
		stack = ItemStack.EMPTY;
		lazyStackString = s;
	}

	public ItemStack getStack()
	{
		if (!lazyStackString.isEmpty())
		{
			stack = ItemStackSerializer.parseItem(lazyStackString);
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