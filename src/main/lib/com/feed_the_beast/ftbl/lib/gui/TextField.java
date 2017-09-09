package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.MouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TextField extends Button
{
	public List<String> text = Collections.emptyList();

	public TextField(int x, int y, int width, int height, FontRenderer font, String txt)
	{
		super(x, y, width, height);

		if (!txt.isEmpty())
		{
			if (width >= 10)
			{
				text = font.listFormattedStringToWidth(txt, width);
			}
			else
			{
				text = Collections.singletonList(txt);
			}
		}

		if (height <= 0)
		{
			int h = font.FONT_HEIGHT + 1;
			setHeight(text.isEmpty() ? h : h * text.size());
		}

		if (text.size() == 1 && !text.get(0).isEmpty())
		{
			setWidth(font.getStringWidth(text.get(0)));
		}
	}

	@Override
	public void addMouseOverText(GuiBase gui, List<String> list)
	{
	}

	@Override
	public void onClicked(GuiBase gui, MouseButton button)
	{
	}

	@Override
	public void renderWidget(GuiBase gui)
	{
		if (text.isEmpty())
		{
			return;
		}

		int ay = getAY();
		int ax = getAX();

		for (int i = 0; i < text.size(); i++)
		{
			gui.drawString(text.get(i), ax, ay + i * 10 + 1);
		}

		GlStateManager.color(1F, 1F, 1F, 1F);
	}
}
