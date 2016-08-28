package com.feed_the_beast.ftbl.api.gui.widgets;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.latmod.lib.ITextureCoords;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public class WidgetLM
{
    public int posX, posY, width, height;
    private WidgetLM parentWidget;

    public WidgetLM(int x, int y, int w, int h)
    {
        posX = x;
        posY = y;
        width = w;
        height = h;
    }

    public WidgetLM getParentWidget()
    {
        return parentWidget;
    }

    public void setParentWidget(WidgetLM p)
    {
        parentWidget = p;
    }

    public boolean isEnabled()
    {
        return true;
    }

    public boolean shouldRender(GuiLM gui)
    {
        return gui.isInside(this);
    }

    public boolean isInside(WidgetLM w)
    {
        double a0 = getAY();
        double a1 = w.getAY();

        if(a1 + w.height >= a0 || a1 <= a0 + height)
        {
            return true;
        }

        a0 = getAX();
        a1 = w.getAX();

        return (a1 + w.width < a0 && a1 > a0 + width);
    }

    public int getAX()
    {
        return (parentWidget == null) ? posX : (parentWidget.getAX() + posX);
    }

    public int getAY()
    {
        return (parentWidget == null) ? posY : (parentWidget.getAY() + posY);
    }

    public final void render(ITextureCoords icon)
    {
        GuiLM.render(icon, getAX(), getAY(), width, height);
    }

    public void mousePressed(GuiLM gui, IMouseButton button)
    {
    }

    public boolean keyPressed(GuiLM gui, int key, char keyChar)
    {
        return false;
    }

    public void addMouseOverText(GuiLM gui, List<String> l)
    {
        String t = getTitle(gui);

        if(t != null)
        {
            l.add(t);
        }
    }

    public void renderWidget(GuiLM gui)
    {
    }

    @Nullable
    public String getTitle(GuiLM gui)
    {
        return null;
    }
}