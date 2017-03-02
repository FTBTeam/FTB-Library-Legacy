package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IPanel;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class PanelScrollBar extends SliderLM
{
    public final IPanel panel;
    private int elementSize;
    private double scrollStep;

    public PanelScrollBar(int x, int y, int w, int h, int ss, IPanel p)
    {
        super(x, y, w, h, ss);
        panel = p;
    }

    public void setElementSize(int s)
    {
        elementSize = s;

        if(panel.getWidgets().isEmpty())
        {
            setScrollStep(0);
        }
        else
        {
            setSrollStepFromOneElementSize(elementSize / panel.getWidgets().size());
        }
    }

    public void setScrollStep(double v)
    {
        scrollStep = v;
    }

    public void setSrollStepFromOneElementSize(int s)
    {
        setScrollStep(s / (double) (elementSize - panel.getHeight()));
    }

    @Override
    public boolean canMouseScroll(IGui gui)
    {
        return super.canMouseScroll(gui) || gui.isMouseOver(panel);
    }

    @Override
    public double getScrollStep()
    {
        return scrollStep;
    }

    @Override
    public void onMoved(IGui gui)
    {
        if(getDirection().isVertical())
        {
            panel.setScrollY(getValue(gui), elementSize);
        }
        else
        {
            panel.setScrollX(getValue(gui), elementSize);
        }
    }

    @Override
    public boolean isEnabled(IGui gui)
    {
        return elementSize > (getDirection().isVertical() ? panel.getHeight() : panel.getHeight());
    }

    @Override
    public boolean shouldRender(IGui gui)
    {
        return isEnabled(gui);
    }
}