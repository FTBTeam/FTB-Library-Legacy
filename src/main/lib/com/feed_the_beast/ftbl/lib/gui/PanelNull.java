package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.MouseButton;

import java.util.List;

/**
 * @author LatvianModder
 */
public class PanelNull extends Panel
{
	public static final PanelNull INSTANCE = new PanelNull();

	public PanelNull()
	{
		super(0, 0, 0, 0);
	}

	@Override
	public Panel getParentPanel()
	{
		return this;
	}

	@Override
	public void setParentPanel(Panel p)
	{
	}

	@Override
	public void setY(int v)
	{
	}

	@Override
	public void setWidth(int v)
	{
	}

	@Override
	public void setHeight(int v)
	{
	}

	@Override
	public int getAX()
	{
		return 0;
	}

	@Override
	public int getAY()
	{
		return 0;
	}

	@Override
	public void addWidgets()
	{
	}

	@Override
	public void refreshWidgets()
	{
	}

	@Override
	public boolean collidesWith(int x, int y, int w, int h)
	{
		return false;
	}

	@Override
	public boolean isEnabled(GuiBase gui)
	{
		return false;
	}

	@Override
	public boolean shouldRender(GuiBase gui)
	{
		return false;
	}

	@Override
	public boolean mousePressed(GuiBase gui, MouseButton button)
	{
		return false;
	}

	@Override
	public void mouseReleased(GuiBase gui)
	{
	}

	@Override
	public boolean keyPressed(GuiBase gui, int key, char keyChar)
	{
		return false;
	}

	@Override
	public void addMouseOverText(GuiBase gui, List<String> list)
	{
	}

	@Override
	public void renderWidget(GuiBase gui)
	{
	}

	@Override
	public void setScrollX(int scroll)
	{
	}

	@Override
	public void setScrollY(int scroll)
	{
	}

	@Override
	public boolean hasFlag(int flag)
	{
		return false;
	}
}