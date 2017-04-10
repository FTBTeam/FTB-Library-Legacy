package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;

public abstract class Button extends Widget
{
    public static final TexturelessRectangle DEFAULT_BACKGROUND = new TexturelessRectangle(Color4I.NONE).setLineColor(new Color4I(false, 0xFFC0C0C0));
    public static final IDrawableObject DEFAULT_MOUSE_OVER = new TexturelessRectangle(new Color4I(false, 0x64E4FFFF));

    private String title = "";
    private IDrawableObject icon = ImageProvider.NULL;

    public Button(int x, int y, int w, int h)
    {
        super(x, y, w, h);
    }

    public Button(int x, int y, int w, int h, String t)
    {
        this(x, y, w, h);
        setTitle(t);
    }

    @Override
    public String getTitle(GuiBase gui)
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
    public IDrawableObject getIcon(GuiBase gui)
    {
        return icon;
    }

    @Override
    public void mousePressed(GuiBase gui, IMouseButton button)
    {
        if(gui.isMouseOver(this))
        {
            onClicked(gui, button);
        }
    }

    public abstract void onClicked(GuiBase gui, IMouseButton button);
}