package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ItemIcon extends Icon
{
	private static class LazyItemIcon extends ItemIcon
	{
		private String lazyStackString;
		private boolean createdStack;

		private LazyItemIcon(String s)
		{
			super(ItemStack.EMPTY);
			lazyStackString = s;
		}

		@Override
		public ItemStack getStack()
		{
			if (!createdStack)
			{
				stack = ItemStackSerializer.parseItem(lazyStackString);
				createdStack = true;
			}

			return stack;
		}

		@Override
		public JsonElement getJson()
		{
			return new JsonPrimitive("item:" + lazyStackString);
		}
	}

	ItemStack stack;

	public static Icon getItemIcon(ItemStack stack)
	{
		return stack.isEmpty() ? EMPTY : new ItemIcon(stack);
	}

	public static Icon getItemIcon(String lazyStackString)
	{
		return lazyStackString.isEmpty() ? EMPTY : new LazyItemIcon(lazyStackString);
	}

	private ItemIcon(ItemStack is)
	{
		stack = is;
	}

	public ItemStack getStack()
	{
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