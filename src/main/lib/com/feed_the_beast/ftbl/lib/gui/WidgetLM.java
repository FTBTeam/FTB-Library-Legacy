package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

public class WidgetLM implements IWidget
{
    public int posX, posY;
    private int width, height;
    private IPanel parentPanel = PanelNull.INSTANCE;

    public WidgetLM(int x, int y, int w, int h)
    {
        posX = x;
        posY = y;
        width = w;
        height = h;
    }

    @Override
    public final int getX()
    {
        return posX;
    }

    @Override
    public final int getY()
    {
        return posY;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public final void setX(int v)
    {
        posX = v;
    }

    @Override
    public final void setY(int v)
    {
        posY = v;
    }

    @Override
    public void setWidth(int v)
    {
        width = v;
    }

    @Override
    public void setHeight(int v)
    {
        height = v;
    }

    @Override
    public IPanel getParentPanel()
    {
        return parentPanel;
    }

    @Override
    public void setParentPanel(IPanel p)
    {
        parentPanel = p;
    }

    public int renderTitleInCenter()
    {
        return 0;
    }

    public String getTitle(IGui gui)
    {
        return "";
    }

    public IDrawableObject getIcon(IGui gui)
    {
        return ImageProvider.NULL;
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> list)
    {
        int col = renderTitleInCenter();

        if(col != 0)
        {
            return;
        }

        String t = getTitle(gui);

        if(!t.isEmpty())
        {
            list.add(t);
        }
    }

    @Override
    public void renderWidget(IGui gui)
    {
        int col = renderTitleInCenter();

        if(col != 0)
        {
            String t = getTitle(gui);

            if(!t.isEmpty())
            {
                GuiHelper.drawCenteredString(gui.getFont(), t, getAX() + getWidth() / 2, getAY() + getHeight() / 2, col);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }
        }
        else
        {
            getIcon(gui).draw(this);
        }
    }
}