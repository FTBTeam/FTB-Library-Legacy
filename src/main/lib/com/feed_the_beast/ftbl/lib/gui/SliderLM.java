package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class SliderLM extends WidgetLM
{
    public static final IDrawableObject DEFAULT_SLIDER = new TexturelessRectangle(0x99666666);
    public static final IDrawableObject DEFAULT_BACKGROUND = new TexturelessRectangle(0x99333333);

    public final int sliderSize;
    private double value;
    private boolean isGrabbed;
    public IDrawableObject slider = DEFAULT_SLIDER, background = DEFAULT_BACKGROUND;

    public SliderLM(int x, int y, int w, int h, int ss)
    {
        super(x, y, w, h);
        sliderSize = ss;
    }

    @Override
    public void mousePressed(IGui gui, IMouseButton button)
    {
        if(gui.isMouseOver(this))
        {
            setGrabbed(gui, true);
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> list)
    {
        double min = getDisplayMin();
        double max = getDisplayMax();

        if(min < max)
        {
            String s = "" + (int) MathHelperLM.map(value, 0D, 1D, min, max);
            String t = getTitle(gui);
            list.add(t.isEmpty() ? s : (t + ": " + s));
        }
    }

    @Override
    public void renderWidget(IGui gui)
    {
        int ax = getAX();
        int ay = getAY();
        int w = getWidth();
        int h = getHeight();

        if(isEnabled(gui))
        {
            double v = getValue(gui);
            double v0 = v;

            if(isGrabbed(gui))
            {
                if(gui.isMouseButtonDown(0))
                {
                    if(getDirection().isVertical())
                    {
                        v = (gui.getMouseY() - (ay + (sliderSize / 2D))) / (double) (h - sliderSize);
                    }
                    else
                    {
                        v = (gui.getMouseX() - (ax + (sliderSize / 2D))) / (double) (w - sliderSize);
                    }
                }
                else
                {
                    setGrabbed(gui, false);
                }
            }

            if(gui.getMouseWheel() != 0 && canMouseScroll(gui))
            {
                v += (gui.getMouseWheel() < 0) ? getScrollStep() : -getScrollStep();
            }

            v = MathHelper.clamp(v, 0D, 1D);

            if(v0 != v)
            {
                setValue(gui, v);
            }
        }

        background.draw(ax, ay, w, h);

        if(getDirection().isVertical())
        {
            slider.draw(ax, ay + getValueI(gui, h), w, sliderSize);
        }
        else
        {
            slider.draw(ax + getValueI(gui, w), ay, sliderSize, h);
        }
    }

    public boolean isGrabbed(IGui gui)
    {
        return isGrabbed;
    }

    public void setGrabbed(IGui gui, boolean b)
    {
        isGrabbed = b;
    }

    public void onMoved(IGui gui)
    {
    }

    public boolean canMouseScroll(IGui gui)
    {
        return gui.isMouseOver(this);
    }

    public void setValue(IGui gui, double v)
    {
        if(value != v)
        {
            value = MathHelper.clamp(v, 0D, 1D);
            onMoved(gui);
        }
    }

    public double getValue(IGui gui)
    {
        return value;
    }

    public int getValueI(IGui gui, int max)
    {
        return (int) (getValue(gui) * (max - sliderSize));
    }

    public double getScrollStep()
    {
        return 0.1D;
    }

    public EnumDirection getDirection()
    {
        return EnumDirection.VERTICAL;
    }

    public double getDisplayMin()
    {
        return 0;
    }

    public double getDisplayMax()
    {
        return 0;
    }
}