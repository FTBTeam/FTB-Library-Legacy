package com.feed_the_beast.ftbl.api.client.gui.widgets;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public abstract class ButtonLM extends WidgetLM
{
    public ButtonLM(double x, double y, double w, double h)
    {
        super(x, y, w, h);
    }

    public ButtonLM(double x, double y, double w, double h, String t)
    {
        this(x, y, w, h);
        title = t;
    }

    @Override
    public void mousePressed(GuiLM gui, MouseButton button)
    {
        if(gui.isMouseOver(this))
        {
            onClicked(gui, button);
        }
    }

    public abstract void onClicked(GuiLM gui, MouseButton button);
}