package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.latmod.lib.ITextureCoords;
import com.latmod.lib.math.MathHelperLM;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.List;

@SideOnly(Side.CLIENT)
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
    public void mousePressed(GuiLM gui, IMouseButton b)
    {
        if(b.isLeft() && gui.isMouseOver(this))
        {
            setGrabbed(gui, true);
        }
    }

    @Override
    public void addMouseOverText(GuiLM gui, List<String> l)
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

    public void updateSlider(GuiLM gui)
    {
        double v = getValue(gui);
        double v0 = v;

        if(isGrabbed(gui))
        {
            if(Mouse.isButtonDown(0))
            {
                if(getDirection().isVertical())
                {
                    v = (gui.mouseY - (getAY() + (sliderSize / 2D))) / (double) (height - sliderSize);
                }
                else
                {
                    v = (gui.mouseX - (getAX() + (sliderSize / 2D))) / (double) (width - sliderSize);
                }
            }
            else
            {
                setGrabbed(gui, false);
            }
        }

        if(gui.dmouseWheel != 0 && canMouseScroll(gui))
        {
            v += (gui.dmouseWheel < 0) ? getScrollStep() : -getScrollStep();
        }

        v = MathHelper.clamp_double(v, 0D, 1D);

        if(v0 != v)
        {
            setValue(gui, v);
        }
    }

    public boolean isGrabbed(GuiLM gui)
    {
        return isGrabbed;
    }

    public void setGrabbed(GuiLM gui, boolean b)
    {
        isGrabbed = b;
    }

    public void onMoved(GuiLM gui)
    {
    }

    public boolean canMouseScroll(GuiLM gui)
    {
        return gui.isMouseOver(this);
    }

    public void setValue(GuiLM gui, double v)
    {
        if(value != v)
        {
            value = v;
            onMoved(gui);
        }
    }

    public double getValue(GuiLM gui)
    {
        return value;
    }

    public int getValueI()
    {
        return (int) (value * ((getDirection().isVertical() ? height : width) - sliderSize));
    }

    public void renderSlider(ITextureCoords tc)
    {
        if(getDirection().isVertical())
        {
            GuiLM.render(tc, getAX(), getAY() + getValueI(), width, sliderSize);
        }
        else
        {
            GuiLM.render(tc, getAX() + getValueI(), getAY(), sliderSize, height);
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