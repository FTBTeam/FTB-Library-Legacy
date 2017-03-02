package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 13.02.2017.
 */
public enum PanelNull implements IPanel
{
    INSTANCE;

    @Override
    public IPanel getParentPanel()
    {
        return this;
    }

    @Override
    public void setParentPanel(IPanel p)
    {
    }

    @Override
    public int getX()
    {
        return 0;
    }

    @Override
    public int getY()
    {
        return 0;
    }

    @Override
    public int getWidth()
    {
        return 1;
    }

    @Override
    public int getHeight()
    {
        return 1;
    }

    @Override
    public void setX(int v)
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
    public Collection<IWidget> getWidgets()
    {
        return Collections.emptyList();
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
    public boolean isEnabled(IGui gui)
    {
        return false;
    }

    @Override
    public boolean shouldRender(IGui gui)
    {
        return false;
    }

    @Override
    public void mousePressed(IGui gui, IMouseButton button)
    {
    }

    @Override
    public void mouseReleased(IGui gui)
    {
    }

    @Override
    public boolean keyPressed(IGui gui, int key, char keyChar)
    {
        return false;
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> list)
    {
    }

    @Override
    public void renderWidget(IGui gui)
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