package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class PanelLM extends WidgetLM implements IPanel
{
    private final List<IWidget> widgets;

    public PanelLM(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        widgets = new ArrayList<>();
    }

    @Override
    public Collection<IWidget> getWidgets()
    {
        return widgets;
    }

    public abstract void addWidgets();

    @Override
    public void refreshWidgets()
    {
        getWidgets().clear();
        addWidgets();
    }
}