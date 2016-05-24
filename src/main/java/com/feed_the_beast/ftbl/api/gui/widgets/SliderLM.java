package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IGuiLM;
import com.feed_the_beast.ftbl.util.TextureCoords;
import latmod.lib.MathHelperLM;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.List;

@SideOnly(Side.CLIENT)
public class SliderLM extends WidgetLM
{
    public final int sliderSize;
    public boolean isGrabbed;
    public float value;
    public int displayMin = 0;
    public int displayMax = 0;
    public boolean isVertical = false;
    public float scrollStep = 0.1F;

    public SliderLM(IGuiLM g, int x, int y, int w, int h, int ss)
    {
        super(g, x, y, w, h);
        sliderSize = ss;
    }

    public void update()
    {
        float v0 = value;

        if(isGrabbed)
        {
            if(Mouse.isButtonDown(0))
            {
                if(isVertical)
                {
                    value = (gui.mouse().y - (getAY() + (sliderSize / 2F))) / (float) (height - sliderSize);
                }
                else
                {
                    value = (gui.mouse().x - (getAX() + (sliderSize / 2F))) / (float) (width - sliderSize);
                }
            }
            else
            {
                isGrabbed = false;
                onReleased();
            }
        }

        if(gui.mouse().dwheel != 0 && canMouseScroll())
        {
            value += (gui.mouse().dwheel < 0) ? scrollStep : -scrollStep;
        }

        value = MathHelperLM.clampFloat(value, 0F, 1F);

        if(v0 != value)
        {
            onMoved();
        }
    }

    public void onMoved()
    {
    }

    public void onReleased()
    {
    }

    public boolean canMouseScroll()
    {
        return mouseOver();
    }

    public int getValueI()
    {
        return (int) (value * ((isVertical ? height : width) - sliderSize));
    }

    public void renderSlider(TextureCoords tc)
    {
        if(isVertical)
        {
            GuiLM.render(tc, getAX(), getAY() + getValueI(), gui.getZLevel(), width, sliderSize);
        }
        else
        {
            GuiLM.render(tc, getAX() + getValueI(), getAY(), gui.getZLevel(), sliderSize, height);
        }
    }

    @Override
    public void mousePressed(MouseButton b)
    {
        if(mouseOver() && b.isLeft())
        {
            isGrabbed = true;
        }
    }

    @Override
    public void addMouseOverText(List<String> l)
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