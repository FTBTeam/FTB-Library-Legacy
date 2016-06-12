package com.feed_the_beast.ftbl.api.client.gui.widgets;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class WidgetLM
{
    public double posX, posY, widthW, heightW;
    public PanelLM parentPanel = null;
    public String title = null;

    public WidgetLM(double x, double y, double w, double h)
    {
        posX = x;
        posY = y;
        widthW = w;
        heightW = h;
    }

    public boolean isEnabled()
    {
        return true;
    }

    public boolean shouldRender(GuiLM gui)
    {
        return gui.isVisible(this);
    }

    public double getAX()
    {
        return (parentPanel == null) ? posX : (parentPanel.getAX() + posX);
    }

    public double getAY()
    {
        return (parentPanel == null) ? posY : (parentPanel.getAY() + posY);
    }

    public final void render(TextureCoords icon)
    {
        GuiLM.render(icon, getAX(), getAY(), widthW, heightW);
    }

    public void mousePressed(GuiLM gui, MouseButton b)
    {
    }

    public boolean keyPressed(GuiLM gui, int key, char keyChar)
    {
        return false;
    }

    public void addMouseOverText(GuiLM gui, List<String> l)
    {
        if(title != null)
        {
            l.add(title);
        }
    }

    public void renderWidget(GuiLM gui)
    {
    }
}