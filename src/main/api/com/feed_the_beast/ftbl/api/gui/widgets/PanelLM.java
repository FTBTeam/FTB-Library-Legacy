package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class PanelLM extends WidgetLM implements IPanel
{
    public final List<IWidget> widgets;

    public PanelLM(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        widgets = new ArrayList<>();
    }

    public abstract void addWidgets();

    @Override
    public void add(IWidget w)
    {
        w.setParentPanel(this);
        widgets.add(w);

        if(w instanceof IPanel)
        {
            ((IPanel) w).refreshWidgets();
        }
    }

    @Override
    public void refreshWidgets()
    {
        widgets.clear();
        addWidgets();
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
        for(IWidget w : widgets)
        {
            if(w.isEnabled() && gui.isMouseOver(w))
            {
                w.addMouseOverText(gui, l);
            }
        }
    }

    @Override
    public void mousePressed(IGui gui, IMouseButton b)
    {
        for(IWidget w : widgets)
        {
            if(w.isEnabled())
            {
                w.mousePressed(gui, b);
            }
        }
    }

    @Override
    public boolean keyPressed(IGui gui, int key, char keyChar)
    {
        for(IWidget w : widgets)
        {
            if(w.isEnabled() && w.keyPressed(gui, key, keyChar))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void renderWidget(IGui gui)
    {
        for(IWidget widget : widgets)
        {
            if(widget.shouldRender(gui))
            {
                widget.renderWidget(gui);
            }
        }
    }
}