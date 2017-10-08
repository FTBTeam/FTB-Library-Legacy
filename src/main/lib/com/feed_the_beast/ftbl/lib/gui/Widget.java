package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

public class Widget
{
	protected static final int DARK = 1;
	protected static final int SHADOW = 2;
	protected static final int CENTERED = 4;

	public GuiBase gui;
	public int posX, posY, width, height;
	private Panel parentPanel;

	public Widget(GuiBase g, int x, int y, int w, int h)
	{
		gui = g;
		posX = x;
		posY = y;
		width = Math.max(w, 1);
		height = Math.max(h, 1);
		parentPanel = gui;
	}

	public void setX(int v)
	{
		posX = v;
	}

	public void setY(int v)
	{
		posY = v;
	}

	public void setWidth(int v)
	{
		width = Math.max(v, 1);
	}

	public void setHeight(int v)
	{
		height = Math.max(v, 1);
	}

	public Panel getParentPanel()
	{
		return parentPanel;
	}

	public void setParentPanel(Panel p)
	{
		parentPanel = p;
	}

	public int getAX()
	{
		return getParentPanel().getAX() + posX;
	}

	public int getAY()
	{
		return getParentPanel().getAY() + posY;
	}

	public boolean collidesWith(int x, int y, int w, int h)
	{
		int ay = getAY();
		if (ay >= y + h || ay + width <= y)
		{
			return false;
		}

		int ax = getAX();
		return ax < x + w && ax + height > x;
	}

	public boolean isEnabled()
	{
		return true;
	}

	public boolean shouldRender()
	{
		return true;
	}

	public Color4I renderTitleInCenter()
	{
		return Icon.EMPTY;
	}

	public String getTitle()
	{
		return "";
	}

	public Icon getIcon()
	{
		return Icon.EMPTY;
	}

	public void addMouseOverText(List<String> list)
	{
		Color4I col = renderTitleInCenter();

		if (!col.isEmpty())
		{
			return;
		}

		String title = getTitle();

		if (!title.isEmpty())
		{
			list.add(title);
		}
	}

	public void renderWidget()
	{
		Color4I col = renderTitleInCenter();

		if (col.isEmpty())
		{
			getIcon().draw(this);
		}
		else
		{
			String title = getTitle();

			if (!title.isEmpty())
			{
				gui.drawString(title, getAX() + width / 2, getAY() + height / 2, col, DARK | CENTERED);
				GlStateManager.color(1F, 1F, 1F, 1F);
			}
		}
	}

	public boolean mousePressed(MouseButton button)
	{
		return false;
	}

	public void mouseReleased()
	{
	}

	public boolean keyPressed(int key, char keyChar)
	{
		return false;
	}
}