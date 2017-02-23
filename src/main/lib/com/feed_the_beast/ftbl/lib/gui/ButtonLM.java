package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;

public abstract class ButtonLM extends WidgetLM
{
    private String title = "";
    private IDrawableObject icon = ImageProvider.NULL;

    public ButtonLM(int x, int y, int w, int h)
    {
        super(x, y, w, h);
    }

    public ButtonLM(int x, int y, int w, int h, String t)
    {
        this(x, y, w, h);
        setTitle(t);
    }

    @Override
    public String getTitle(IGui gui)
    {
        return title;
    }

    public void setTitle(String s)
    {
        title = s;
    }

    public void setIcon(IDrawableObject i)
    {
        icon = i;
    }

    @Override
    public IDrawableObject getIcon(IGui gui)
    {
        return icon;
    }

    @Override
    public void mousePressed(IGui gui, IMouseButton button)
    {
        if(gui.isMouseOver(this))
        {
            onClicked(gui, button);
        }
    }

    public abstract void onClicked(IGui gui, IMouseButton button);
}