package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.latmod.lib.TextureCoords;
import com.latmod.lib.math.MathHelperLM;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class SliderLM extends WidgetLM
{
    public final int sliderSize;
    public double value;
    public int displayMin = 0;
    public int displayMax = 0;
    public boolean isVertical = false;
    public double scrollStep = 0.1D;
    private boolean isGrabbed;

    public SliderLM(int x, int y, int w, int h, int ss)
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
                    value = (gui.mouseY - (getAY() + (sliderSize / 2D))) / (double) (height - sliderSize);
                }
                else
                {
                    value = (gui.mouseX - (getAX() + (sliderSize / 2D))) / (double) (width - sliderSize);
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

    public final boolean isGrabbed()
    {
        return isGrabbed;
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
        return (int) (value * ((isVertical ? height : width) - sliderSize));
    }

    public void renderSlider(TextureCoords tc)
    {
        if(isVertical)
        {
            GuiLM.render(tc, getAX(), getAY() + getValueI(), width, sliderSize);
        }
        else
        {
            GuiLM.render(tc, getAX() + getValueI(), getAY(), sliderSize, height);
        }
    }

    @Override
    public void mousePressed(GuiLM gui, IMouseButton b)
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