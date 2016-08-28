package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
    public String getTitle(GuiLM gui)
    {
        return title;
    }

    public void setTitle(String s)
    {
        title = s;
    }

    @Override
    public void mousePressed(GuiLM gui, IMouseButton button)
    {
        if(gui.isMouseOver(this))
        {
            onClicked(gui, button);
        }
    }

    public abstract void onClicked(GuiLM gui, IMouseButton button);
}