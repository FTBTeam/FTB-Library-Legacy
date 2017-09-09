package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

public class Widget
{
	public int posX, posY, width, height;
	private Panel parentPanel = PanelNull.INSTANCE;

	public Widget(int x, int y, int w, int h)
	{
		posX = x;
		posY = y;
		width = Math.max(w, 1);
		height = Math.max(h, 1);
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

	public boolean isEnabled(GuiBase gui)
	{
		return true;
	}

	public boolean shouldRender(GuiBase gui)
	{
		return true;
	}

	public Color4I renderTitleInCenter(GuiBase gui)
	{
		return Color4I.NONE;
	}

	public String getTitle(GuiBase gui)
	{
		return "";
	}

	public Icon getIcon(GuiBase gui)
	{
		return Icon.EMPTY;
	}

	public void addMouseOverText(GuiBase gui, List<String> list)
	{
		Color4I col = renderTitleInCenter(gui);

		if (col.hasColor())
		{
			return;
		}

		String t = getTitle(gui);

		if (!t.isEmpty())
		{
			list.add(t);
		}
	}

	public void renderWidget(GuiBase gui)
	{
		getIcon(gui).draw(this, Color4I.NONE);

		Color4I col = renderTitleInCenter(gui);

		if (col.hasColor())
		{
			String t = getTitle(gui);

			if (!t.isEmpty())
			{
				gui.drawCenteredString(t, getAX() + width / 2, getAY() + height / 2, col);
				GlStateManager.color(1F, 1F, 1F, 1F);
			}
		}
	}

	public boolean mousePressed(GuiBase gui, MouseButton button)
	{
		return false;
	}

	public void mouseReleased(GuiBase gui)
	{
	}

	public boolean keyPressed(GuiBase gui, int key, char keyChar)
	{
		return false;
	}
}