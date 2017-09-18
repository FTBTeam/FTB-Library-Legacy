package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.LangKey;
import com.feed_the_beast.ftbl.lib.MouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TextField extends Button
{
	public List<String> text = Collections.emptyList();

	public TextField(int x, int y, int w, int h, FontRenderer font, String txt)
	{
		super(x, y, w, h);

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

		if (height <= 1)
		{
			int h1 = font.FONT_HEIGHT + 1;
			setHeight(text.isEmpty() ? h1 : h1 * text.size());
		}

		if (text.size() == 1 && !text.get(0).isEmpty())
		{
			setWidth(font.getStringWidth(text.get(0)));
		}
	}

	public TextField(int x, int y, int w, int h, FontRenderer font, LangKey key)
	{
		this(x, y, w, h, font, key.translate());
	}

	public TextField(int x, int y, int w, int h, FontRenderer font, ITextComponent component)
	{
		this(x, y, w, h, font, component.getFormattedText());
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
