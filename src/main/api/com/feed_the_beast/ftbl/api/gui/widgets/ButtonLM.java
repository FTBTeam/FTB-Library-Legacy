package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public abstract class ButtonLM extends WidgetLM
{
    public ButtonLM(int x, int y, int w, int h)
    {
        super(x, y, w, h);
    }

    public ButtonLM(int x, int y, int w, int h, String t)
    {
        this(x, y, w, h);
        title = t;
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