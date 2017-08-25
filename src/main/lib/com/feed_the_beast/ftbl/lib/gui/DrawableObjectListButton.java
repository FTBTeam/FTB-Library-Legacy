package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.client.DrawableItem;
import com.feed_the_beast.ftbl.lib.client.IconAnimation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import java.util.List;

public class DrawableObjectListButton extends Button
{
	private final IconAnimation list;
	private final int cols;

	public DrawableObjectListButton(int x, int y, IconAnimation i, int c)
	{
		super(x, y, 16, 16);
		list = i;

		cols = c;
		setWidth(cols == 0 ? 16 : (4 + Math.min(cols, i.getItemCount()) * 16));
		setHeight(cols == 0 ? 16 : (4 + (i.getItemCount() / cols + 1) * 16));
	}

	@Override
	public void renderWidget(GuiBase gui)
	{
		int ax = getAX();
		int ay = getAY();

		if (cols == 0)
		{
			list.draw(ax, ay, 16, 16, Color4I.NONE);
		}
		else
		{
			for (int i = list.getItemCount() - 1; i >= 0; i--)
			{
				list.setIndex(i);
				list.draw(ax + 2 + (i % cols) * 16, ay + 2 + (i / cols) * 16, 16, 16, Color4I.NONE);
			}
		}
	}

	@Override
	public void addMouseOverText(GuiBase gui, List<String> l)
	{
		int index = -1;

		if (cols > 0)
		{
			int mx = gui.getMouseX() - getAX() - 2;
			int my = gui.getMouseY() - getAY() - 2;

			if (mx < 0 || my < 0 || mx >= width - 4 || my >= height - 4)
			{
				return;
			}

			index = ((mx / 16) % cols + (my / 16) * cols);
		}

		IDrawableObject object = list.getObject(index);

		if (object instanceof DrawableItem)
		{
			ItemStack stack = ((DrawableItem) object).stack;
			l.add(stack.getDisplayName());
			stack.getItem().addInformation(stack, ClientUtils.MC.world, l, ITooltipFlag.TooltipFlags.NORMAL);
		}
	}

	@Override
	public void onClicked(GuiBase gui, IMouseButton button)
	{
		/*
		if (cols == 0)
		{
			//getStack(-1)
		}
		else
		{
			//int x = gui.getMouseX() - 2;
			//int y = gui.getMouseY() - 2;
		}
		*/
	}
}