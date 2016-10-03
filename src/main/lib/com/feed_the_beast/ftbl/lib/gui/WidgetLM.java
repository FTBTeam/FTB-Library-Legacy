package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.lib.client.ITextureCoords;

import javax.annotation.Nullable;
import java.util.List;

public class WidgetLM implements IWidget
{
    public int posX, posY;
    private int width, height;
    private IPanel parentPanel;

    public WidgetLM(int x, int y, int w, int h)
    {
        posX = x;
        posY = y;
        width = w;
        height = h;
    }

    @Override
    public final int getX()
    {
        return posX;
    }

    @Override
    public final int getY()
    {
        return posY;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public final void setX(int v)
    {
        posX = v;
    }

    @Override
    public final void setY(int v)
    {
        posY = v;
    }

    @Override
    public void setWidth(int v)
    {
        width = v;
    }

    @Override
    public void setHeight(int v)
    {
        height = v;
    }

    @Override
    @Nullable
    public IPanel getParentPanel()
    {
        return parentPanel;
    }

    @Override
    public void setParentPanel(@Nullable IPanel p)
    {
        parentPanel = p;
    }

    public final void render(ITextureCoords icon)
    {
        GuiHelper.render(icon, getAX(), getAY(), getWidth(), getHeight());
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
        String t = getTitle(gui);

        if(t != null)
        {
            l.add(t);
        }
    }

    @Nullable
    public String getTitle(IGui gui)
    {
        return null;
    }
}