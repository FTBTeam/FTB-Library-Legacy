package com.feed_the_beast.ftbl.lib.gui;

import net.minecraft.util.EnumFacing;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class PanelScrollBar extends Slider
{
    public final Panel panel;
    private int elementSize;
    private double scrollStep;

    public PanelScrollBar(int x, int y, int w, int h, int ss, Panel p)
    {
        super(x, y, w, h, ss);
        panel = p;
    }

    public void setElementSize(int s)
    {
        elementSize = s;

        if(panel.widgets.isEmpty())
        {
            setScrollStep(0);
        }
        else
        {
            setSrollStepFromOneElementSize(elementSize / panel.widgets.size());
        }
    }

    public void setScrollStep(double v)
    {
        scrollStep = v;
    }

    public void setSrollStepFromOneElementSize(int s)
    {
        setScrollStep(s / (double) (elementSize - (getPlane() == EnumFacing.Plane.VERTICAL ? panel.height : panel.width)));
    }

    @Override
    public boolean canMouseScroll(GuiBase gui)
    {
        return super.canMouseScroll(gui) || gui.isMouseOver(panel);
    }

    @Override
    public double getScrollStep()
    {
        return scrollStep;
    }

    @Override
    public void onMoved(GuiBase gui)
    {
        if(getPlane() == EnumFacing.Plane.VERTICAL)
        {
            panel.setScrollY(getValue(gui), elementSize);
        }
        else
        {
            panel.setScrollX(getValue(gui), elementSize);
        }
    }

    @Override
    public boolean isEnabled(GuiBase gui)
    {
        return elementSize > (getPlane() == EnumFacing.Plane.VERTICAL ? panel.height : panel.width);
    }

    @Override
    public boolean shouldRender(GuiBase gui)
    {
        return isEnabled(gui);
    }
}