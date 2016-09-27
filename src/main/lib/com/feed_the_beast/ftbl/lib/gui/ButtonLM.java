package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;

import javax.annotation.Nullable;

public abstract class ButtonLM extends WidgetLM
{
    private String title;

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

    public void setTitle(@Nullable String s)
    {
        title = s;
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