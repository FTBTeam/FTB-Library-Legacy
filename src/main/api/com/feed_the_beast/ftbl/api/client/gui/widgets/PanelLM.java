package com.feed_the_beast.ftbl.api.client.gui.widgets;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public abstract class PanelLM extends WidgetLM
{
    public final List<WidgetLM> widgets;

    public PanelLM(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        widgets = new ArrayList<>();
    }

    public abstract void addWidgets();

    public void add(@Nullable WidgetLM w)
    {
        if(w != null)
        {
            w.parentPanel = this;
            widgets.add(w);

            if(w instanceof PanelLM)
            {
                ((PanelLM) w).refreshWidgets();
            }
        }
    }

    public void addAll(@Nullable Iterable<? extends WidgetLM> l)
    {
        if(l != null)
        {
            for(WidgetLM w : l)
            {
                add(w);
            }
        }
    }

    public void refreshWidgets()
    {
        widgets.clear();
        addWidgets();
    }

    @Override
    public void addMouseOverText(GuiLM gui, List<String> l)
    {
        if(title != null)
        {
            l.add(title);
        }

        for(WidgetLM w : widgets)
        {
            if(w.isEnabled() && gui.isMouseOver(w))
            {
                w.addMouseOverText(gui, l);
            }
        }
    }

    @Override
    public void mousePressed(GuiLM gui, MouseButton b)
    {
        for(WidgetLM w : widgets)
        {
            if(w.isEnabled())
            {
                w.mousePressed(gui, b);
            }
        }
    }

    @Override
    public boolean keyPressed(GuiLM gui, int key, char keyChar)
    {
        for(WidgetLM w : widgets)
        {
            if(w.isEnabled() && w.keyPressed(gui, key, keyChar))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        for(WidgetLM widget : widgets)
        {
            if(widget.shouldRender(gui))
            {
                widget.renderWidget(gui);
            }
        }
    }

    public boolean isInside(WidgetLM w)
    {
        double a0 = getAY();
        double a1 = w.getAY();

        if(a1 + w.height >= a0 || a1 <= a0 + height)
        {
            return true;
        }

        a0 = getAX();
        a1 = w.getAX();

        return (a1 + w.width < a0 && a1 > a0 + width);
    }
}