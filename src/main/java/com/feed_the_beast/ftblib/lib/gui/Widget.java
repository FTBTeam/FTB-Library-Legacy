package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

import java.util.List;

public class Widget
{
	protected static final int DARK = 1;
	protected static final int SHADOW = 2;
	protected static final int CENTERED = 4;
	public static final int UNICODE = 8;
	protected static final int MOUSE_OVER = 16;

	public GuiBase gui;
	public int posX, posY, width, height;
	public Panel parent;

	public Widget(GuiBase _gui, int x, int y, int w, int h)
	{
		gui = _gui;
		posX = x;
		posY = y;
		width = Math.max(w, 0);
		height = Math.max(h, 0);
		parent = gui;
	}

	public Widget(GuiBase gui)
	{
		this(gui, 0, 0, 0, 0);
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
		width = Math.max(v, 0);
	}

	public void setHeight(int v)
	{
		height = Math.max(v, 0);
	}

	public int getAX()
	{
		return parent.getAX() + posX;
	}

	public int getAY()
	{
		return parent.getAY() + posY;
	}

	public boolean collidesWith(int x, int y, int w, int h)
	{
		int ay = getAY();
		if (ay >= y + h || ay + height <= y)
		{
			return false;
		}

		int ax = getAX();
		return ax < x + w && ax + width > x;
	}

	public boolean isEnabled()
	{
		return true;
	}

	public boolean shouldDraw()
	{
		return true;
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
		String title = getTitle();

		if (!title.isEmpty())
		{
			list.add(title);
		}
	}

	public boolean shouldAddMouseOverText()
	{
		return isEnabled() && gui.isMouseOver(this);
	}

	public void draw()
	{
		getIcon().draw(getAX(), getAY(), width, height);
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

	public final int getMouseOverFlag(int ax, int ay)
	{
		return gui.isMouseOver(ax, ay, width, height) ? MOUSE_OVER : 0;
	}
}