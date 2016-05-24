package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.gui.IGuiLM;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class PanelLM extends WidgetLM // GuiLM
{
    public final List<WidgetLM> widgets;

    public PanelLM(IGuiLM g, int x, int y, int w, int h)
    {
        super(g, x, y, w, h);
        widgets = new ArrayList<>();
    }

    public abstract void addWidgets();

    public void add(WidgetLM w)
    {
        if(w == null)
        {
            return;
        }
        w.parentPanel = this;
        widgets.add(w);

        if(w instanceof PanelLM)
        {
            ((PanelLM) w).refreshWidgets();
        }
    }

    public void addAll(Iterable<? extends WidgetLM> l)
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
    public void addMouseOverText(List<String> l)
    {
        if(title != null)
        {
            l.add(title);
        }

        for(WidgetLM w : widgets)
        {
            if(w.isEnabled() && w.mouseOver())
            {
                w.addMouseOverText(l);
            }
        }
    }

    @Override
    public void mousePressed(MouseButton b)
    {
        for(WidgetLM w : widgets)
        {
            if(w.isEnabled())
            {
                w.mousePressed(b);
            }
        }
    }

    @Override
    public boolean keyPressed(int key, char keyChar)
    {
        for(WidgetLM w : widgets)
        {
            if(w.isEnabled() && w.keyPressed(key, keyChar))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void renderWidget()
    {
        for(WidgetLM widget : widgets) { widget.renderWidget(); }
    }
}