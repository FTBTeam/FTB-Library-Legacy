package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.icon.IconAnimation;
import com.feed_the_beast.ftbl.lib.icon.ItemIcon;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import java.util.List;

public class IconAnimationButton extends Button
{
	private final IconAnimation list;
	private final int cols;

	public IconAnimationButton(GuiBase gui, int x, int y, IconAnimation i, int c)
	{
		super(gui, x, y, 16, 16);
		list = i;

		cols = c;
		setWidth(cols == 0 ? 16 : (4 + Math.min(cols, i.getItemCount()) * 16));
		setHeight(cols == 0 ? 16 : (4 + (i.getItemCount() / cols + 1) * 16));
	}

	@Override
	public void renderWidget()
	{
		int ax = getAX();
		int ay = getAY();

		if (cols == 0)
		{
			list.draw(ax, ay, 16, 16);
		}
		else
		{
			for (int i = list.getItemCount() - 1; i >= 0; i--)
			{
				list.setIndex(i);
				list.draw(ax + 2 + (i % cols) * 16, ay + 2 + (i / cols) * 16, 16, 16);
			}
		}
	}

	@Override
	public void addMouseOverText(List<String> l)
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

		Icon object = list.getObject(index);

		if (object instanceof ItemIcon)
		{
			ItemStack stack = ((ItemIcon) object).getStack();
			l.add(stack.getDisplayName());
			stack.getItem().addInformation(stack, ClientUtils.MC.world, l, ITooltipFlag.TooltipFlags.NORMAL);
		}
	}

	@Override
	public void onClicked(MouseButton button)
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