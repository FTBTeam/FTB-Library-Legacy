package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IPanel;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class PanelScrollBar extends SliderLM
{
    public final IPanel panel;
    public int elementSize;
    public int oneElementSize = 16;

    public PanelScrollBar(int x, int y, int w, int h, int ss, IPanel p)
    {
        super(x, y, w, h, ss);
        panel = p;
    }

    @Override
    public boolean canMouseScroll(IGui gui)
    {
        return super.canMouseScroll(gui) || gui.isMouseOver(panel);
    }

    @Override
    public double getScrollStep()
    {
        return (double) oneElementSize / (double) elementSize;
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