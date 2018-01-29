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

	public Widget(GuiBase _gui)
	{
		gui = _gui;
		parent = gui;
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

	public final void setPos(int x, int y)
	{
		setX(x);
		setY(y);
	}

	public final void setSize(int w, int h)
	{
		setWidth(w);
		setHeight(h);
	}

	public final void setPosAndSize(int x, int y, int w, int h)
	{
		setX(x);
		setY(y);
		setWidth(w);
		setHeight(h);
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

	public void mouseReleased(MouseButton button)
	{
	}

	public boolean keyPressed(int key, char keyChar)
	{
		return false;
	}

	public void keyReleased(int key)
	{
	}
}