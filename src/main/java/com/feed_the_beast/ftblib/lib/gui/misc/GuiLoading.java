package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * @author LatvianModder
 */
public class GuiLoading extends GuiBase
{
	private boolean startedLoading = false;
	private boolean isLoading = true;
	private String title;
	public float timer;

	public GuiLoading()
	{
		this("");
	}

	public GuiLoading(String t)
	{
		setSize(128, 128);
		title = t;
	}

	@Override
	public void addWidgets()
	{
	}

	@Override
	public void drawBackground()
	{
		if (!startedLoading)
		{
			startLoading();
			startedLoading = true;
		}

		if (isLoading())
		{
			int ax = getAX();
			int ay = getAY();

			GuiHelper.drawHollowRect(ax + width / 2 - 48, ay + height / 2 - 8, 96, 16, Color4I.WHITE, true);

			int x = ax + width / 2 - 48;
			int y = ay + height / 2 - 8;
			int w = 96;
			int h = 16;

			Color4I col = Color4I.WHITE;
			GlStateManager.disableTexture2D();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

			GuiHelper.addRectToBuffer(buffer, x, y + 1, 1, h - 2, col);
			GuiHelper.addRectToBuffer(buffer, x + w - 1, y + 1, 1, h - 2, col);
			GuiHelper.addRectToBuffer(buffer, x + 1, y, w - 2, 1, col);
			GuiHelper.addRectToBuffer(buffer, x + 1, y + h - 1, w - 2, 1, col);

			x += 1;
			y += 1;
			w -= 2;
			h -= 2;

			timer += ClientUtils.MC.getTickLength();
			timer = timer % (h * 2F);

			for (int oy = 0; oy < h; oy++)
			{
				for (int ox = 0; ox < w; ox++)
				{
					int index = ox + oy + (int) timer;

					if (index % (h * 2) < h)
					{
						col = Color4I.WHITE.withAlpha(200 - (index % h) * 9);

						GuiHelper.addRectToBuffer(buffer, x + ox, y + oy, 1, 1, col);
					}
				}
			}

			tessellator.draw();
			GlStateManager.enableTexture2D();

			String s = getTitle();

			if (!s.isEmpty())
			{
				String[] s1 = s.split("\n");

				for (int i = 0; i < s1.length; i++)
				{
					drawString(s1[i], ax + width / 2, ay - 26 + i * 12, CENTERED);
				}
			}
		}
		else
		{
			closeGui();
			finishLoading();
		}
	}

	@Override
	public synchronized String getTitle()
	{
		return title;
	}

	public synchronized void setTitle(String s)
	{
		title = s;
	}

	public synchronized void setFinished()
	{
		isLoading = false;
	}

	public void startLoading()
	{
	}

	public synchronized boolean isLoading()
	{
		return isLoading;
	}

	public void finishLoading()
	{
	}

	@Override
	public Icon getIcon()
	{
		return Icon.EMPTY;
	}
}