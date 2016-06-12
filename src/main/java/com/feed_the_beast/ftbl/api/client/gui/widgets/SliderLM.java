package com.feed_the_beast.ftbl.api.client.gui.widgets;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.util.TextureCoords;
import latmod.lib.MathHelperLM;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.List;

@SideOnly(Side.CLIENT)
public class SliderLM extends WidgetLM
{
    public final double sliderSize;
    public boolean isGrabbed;
    public double value;
    public int displayMin = 0;
    public int displayMax = 0;
    public boolean isVertical = false;
    public double scrollStep = 0.1D;

    public SliderLM(double x, double y, double w, double h, double ss)
    {
        super(x, y, w, h);
        sliderSize = ss;
    }

    public void update(GuiLM gui)
    {
        double v0 = value;

        if(isGrabbed)
        {
            if(Mouse.isButtonDown(0))
            {
                if(isVertical)
                {
                    value = (gui.mouseY - (getAY() + (sliderSize / 2F))) / (heightW - sliderSize);
                }
                else
                {
                    value = (gui.mouseX - (getAX() + (sliderSize / 2F))) / (widthW - sliderSize);
                }
            }
            else
            {
                isGrabbed = false;
                onReleased(gui);
            }
        }

        if(gui.dmouseWheel != 0 && canMouseScroll(gui))
        {
            value += (gui.dmouseWheel < 0) ? scrollStep : -scrollStep;
        }

        value = MathHelperLM.clamp(value, 0D, 1D);

        if(v0 != value)
        {
            onMoved(gui);
        }
    }

    public void onMoved(GuiLM gui)
    {
    }

    public void onReleased(GuiLM gui)
    {
    }

    public boolean canMouseScroll(GuiLM gui)
    {
        return gui.isMouseOver(this);
    }

    public int getValueI()
    {
        return (int) (value * ((isVertical ? heightW : widthW) - sliderSize));
    }

    public void renderSlider(TextureCoords tc)
    {
        if(isVertical)
        {
            GuiLM.render(tc, getAX(), getAY() + getValueI(), widthW, sliderSize);
        }
        else
        {
            GuiLM.render(tc, getAX() + getValueI(), getAY(), sliderSize, heightW);
        }
    }

    @Override
    public void mousePressed(GuiLM gui, MouseButton b)
    {
        if(b.isLeft() && gui.isMouseOver(this))
        {
            isGrabbed = true;
        }
    }

    @Override
    public void addMouseOverText(GuiLM gui, List<String> l)
    {
        if(displayMin == 0 && displayMax == 0)
        {
            return;
        }
        String s = "" + (int) MathHelperLM.map(value, 0D, 1D, displayMin, displayMax);
        if(title != null)
        {
            s = title + ": " + s;
        }
        l.add(s);
    }
}