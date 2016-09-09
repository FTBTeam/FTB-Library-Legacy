package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.latmod.lib.ITextureCoords;
import com.latmod.lib.math.MathHelperLM;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.List;

public class SliderLM extends WidgetLM
{
    public final int sliderSize;
    private double value;
    private boolean isGrabbed;

    public SliderLM(int x, int y, int w, int h, int ss)
    {
        super(x, y, w, h);
        sliderSize = ss;
    }

    @Override
    public void mousePressed(IGui gui, IMouseButton b)
    {
        if(b.isLeft() && gui.isMouseOver(this))
        {
            setGrabbed(gui, true);
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
        double min = getDisplayMin();
        double max = getDisplayMax();

        if(min < max)
        {
            String s = "" + (int) MathHelperLM.map(value, 0D, 1D, min, max);
            String t = getTitle(gui);
            l.add(t == null ? s : (t + ": " + s));
        }
    }

    public void updateSlider(IGui gui)
    {
        double v = getValue(gui);
        double v0 = v;

        if(isGrabbed(gui))
        {
            if(Mouse.isButtonDown(0))
            {
                if(getDirection().isVertical())
                {
                    v = (gui.getMouseY() - (getAY() + (sliderSize / 2D))) / (double) (getHeight() - sliderSize);
                }
                else
                {
                    v = (gui.getMouseX() - (getAX() + (sliderSize / 2D))) / (double) (getWidth() - sliderSize);
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

        v = MathHelper.clamp_double(v, 0D, 1D);

        if(v0 != v)
        {
            setValue(gui, v);
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
            value = v;
            onMoved(gui);
        }
    }

    public double getValue(IGui gui)
    {
        return value;
    }

    public int getValueI()
    {
        return (int) (value * ((getDirection().isVertical() ? getHeight() : getWidth()) - sliderSize));
    }

    public void renderSlider(ITextureCoords tc)
    {
        if(getDirection().isVertical())
        {
            GuiHelper.render(tc, getAX(), getAY() + getValueI(), getWidth(), sliderSize);
        }
        else
        {
            GuiHelper.render(tc, getAX() + getValueI(), getAY(), sliderSize, getHeight());
        }
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