package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class Slider extends Widget
{
    public static final IDrawableObject DEFAULT_SLIDER = new TexturelessRectangle(new Color4I(false, 0x99666666));
    public static final IDrawableObject DEFAULT_BACKGROUND = new TexturelessRectangle(new Color4I(false, 0x99333333));

    public int sliderSize;
    private double value;
    private boolean isGrabbed;
    public IDrawableObject slider = DEFAULT_SLIDER, background = DEFAULT_BACKGROUND;

    public Slider(int x, int y, int w, int h, int ss)
    {
        super(x, y, w, h);
        sliderSize = ss;
    }

    @Override
    public boolean mousePressed(GuiBase gui, IMouseButton button)
    {
        if(gui.isMouseOver(this))
        {
            setGrabbed(gui, true);
            return true;
        }

        return false;
    }

    @Override
    public void addMouseOverText(GuiBase gui, List<String> list)
    {
        double min = getDisplayMin();
        double max = getDisplayMax();

        if(min < max)
        {
            String s = "" + (int) MathUtils.map(value, 0D, 1D, min, max);
            String t = getTitle(gui);
            list.add(t.isEmpty() ? s : (t + ": " + s));
        }
    }

    @Override
    public void renderWidget(GuiBase gui)
    {
        int ax = getAX();
        int ay = getAY();

        if(isEnabled(gui))
        {
            double v = getValue(gui);
            double v0 = v;

            if(isGrabbed(gui))
            {
                if(gui.isMouseButtonDown(0))
                {
                    if(getPlane() == EnumFacing.Plane.VERTICAL)
                    {
                        v = (gui.getMouseY() - (ay + (sliderSize / 2D))) / (double) (height - sliderSize);
                    }
                    else
                    {
                        v = (gui.getMouseX() - (ax + (sliderSize / 2D))) / (double) (width - sliderSize);
                    }
                }
                else
                {
                    setGrabbed(gui, false);
                }
            }

            if(gui.getMouseWheel() != 0 && canMouseScroll(gui) && gui.isShiftDown() == (getPlane() == EnumFacing.Plane.HORIZONTAL))
            {
                v += (gui.getMouseWheel() < 0) ? getScrollStep() : -getScrollStep();
            }

            v = MathHelper.clamp(v, 0D, 1D);

            if(v0 != v)
            {
                setValue(gui, v);
            }
        }

        background.draw(ax, ay, width, height, Color4I.NONE);

        if(getPlane() == EnumFacing.Plane.VERTICAL)
        {
            slider.draw(ax, ay + getValueI(gui, height), width, sliderSize, Color4I.NONE);
        }
        else
        {
            slider.draw(ax + getValueI(gui, width), ay, sliderSize, height, Color4I.NONE);
        }
    }

    public boolean isGrabbed(GuiBase gui)
    {
        return isGrabbed;
    }

    public void setGrabbed(GuiBase gui, boolean b)
    {
        isGrabbed = b;
    }

    public void onMoved(GuiBase gui)
    {
    }

    public boolean canMouseScroll(GuiBase gui)
    {
        return gui.isMouseOver(this);
    }

    public void setValue(GuiBase gui, double v)
    {
        if(value != v)
        {
            value = MathHelper.clamp(v, 0D, 1D);
            onMoved(gui);
        }
    }

    public double getValue(GuiBase gui)
    {
        return value;
    }

    public int getValueI(GuiBase gui, int max)
    {
        return (int) (getValue(gui) * (max - sliderSize));
    }

    public double getScrollStep()
    {
        return 0.1D;
    }

    public EnumFacing.Plane getPlane()
    {
        return EnumFacing.Plane.VERTICAL;
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