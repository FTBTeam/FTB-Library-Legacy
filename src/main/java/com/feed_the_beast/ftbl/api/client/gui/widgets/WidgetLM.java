package com.feed_the_beast.ftbl.api.client.gui.widgets;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.IGuiLM;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class WidgetLM
{
    public final IGuiLM gui;
    public int posX, posY, width, height;
    public PanelLM parentPanel = null;
    public String title = null;

    public WidgetLM(IGuiLM g, int x, int y, int w, int h)
    {
        gui = g;
        posX = x;
        posY = y;
        width = w;
        height = h;
    }

    public boolean isEnabled()
    {
        return true;
    }

    public boolean shouldRender()
    {
        int i = getAY();

        if(i < -height || i > gui.getMainPanel().height)
        {
            return false;
        }

        i = getAX();

        return i >= -width && i <= gui.getMainPanel().width;
    }

    public int getAX()
    {
        return (parentPanel == null) ? posX : (parentPanel.getAX() + posX);
    }

    public int getAY()
    {
        return (parentPanel == null) ? posY : (parentPanel.getAY() + posY);
    }

    public final int endX()
    {
        return getAX() + width;
    }

    public final int endY()
    {
        return getAY() + height;
    }

    protected boolean mouseOver(int ax, int ay)
    {
        return gui.mouse().isInside(ax, ay, width, height);
    }

    public boolean mouseOver()
    {
        return mouseOver(getAX(), getAY());
    }

    public final void render(TextureCoords icon)
    {
        GuiLM.render(icon, getAX(), getAY(), gui.getZLevel(), width, height);
    }

    public void mousePressed(MouseButton b)
    {
    }

    public boolean keyPressed(int key, char keyChar)
    {
        return false;
    }

    public void addMouseOverText(List<String> l)
    {
        if(title != null)
        {
            l.add(title);
        }
    }

    public void renderWidget()
    {
    }
}