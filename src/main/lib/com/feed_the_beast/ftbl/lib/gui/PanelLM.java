package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class PanelLM extends WidgetLM implements IPanel
{
    private final List<IWidget> widgets;
    private int scrollX = 0, scrollY = 0;
    private int offsetX = 0, offsetY = 0;
    private int flags = 0;

    public PanelLM(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        widgets = new ArrayList<>();
    }

    public void addFlags(int f)
    {
        flags |= f;
    }

    @Override
    public boolean hasFlag(int flag)
    {
        return (flags & flag) != 0;
    }

    @Override
    public Collection<IWidget> getWidgets()
    {
        return widgets;
    }

    public abstract void addWidgets();

    @Override
    public void refreshWidgets()
    {
        getWidgets().clear();
        addWidgets();
        updateWidgetPositions();
    }

    protected int alignWidgets(EnumDirection direction)
    {
        return alignWidgets(direction, 0, 0, 0);
    }

    protected int alignWidgets(EnumDirection direction, int pre, int spacing, int post)
    {
        int i = pre;

        for(IWidget widget : getWidgets())
        {
            if(direction.isVertical())
            {
                widget.setY(i);
                i += widget.getHeight() + spacing;
            }
            else
            {
                widget.setX(i);
                i += widget.getWidth() + spacing;
            }
        }

        if(!getWidgets().isEmpty())
        {
            i -= spacing;
        }

        return i + post;
    }

    @Override
    public int getAX()
    {
        return super.getAX() + offsetX;
    }

    @Override
    public int getAY()
    {
        return super.getAY() + offsetY;
    }

    protected void setOffset(boolean flag)
    {
        if(flag)
        {
            offsetX = -scrollX;
            offsetY = -scrollY;
        }
        else
        {
            offsetX = offsetY = 0;
        }
    }

    @Override
    public void setScrollX(int scroll)
    {
        scrollX = scroll;
    }

    @Override
    public void setScrollY(int scroll)
    {
        scrollY = scroll;
    }

    @Override
    public void renderWidget(IGui gui)
    {
        boolean renderInside = hasFlag(FLAG_ONLY_RENDER_WIDGETS_INSIDE);
        boolean useUnicodeFont = hasFlag(FLAG_UNICODE_FONT);
        boolean unicode = gui.getFont().getUnicodeFlag();
        gui.getFont().setUnicodeFlag(useUnicodeFont);

        int ax = getAX();
        int ay = getAY();
        int w = getWidth();
        int h = getHeight();

        if(renderInside)
        {
            GuiHelper.pushScissor(gui.getScreen(), ax, ay, w, h);
        }

        renderPanelBackground(gui, ax, ay, w, h);

        setOffset(true);

        for(IWidget widget : getWidgets())
        {
            if(widget.shouldRender(gui) && (!renderInside || widget.collidesWith(ax, ay, w, h)))
            {
                renderWidget(gui, widget, ax, ay, w, h);
            }
        }

        setOffset(false);

        if(renderInside)
        {
            GuiHelper.popScissor();
        }

        gui.getFont().setUnicodeFlag(unicode);
    }

    protected void renderPanelBackground(IGui gui, int ax, int ay, int w, int h)
    {
    }

    protected void renderWidget(IGui gui, IWidget widget, int ax, int ay, int w, int h)
    {
        widget.renderWidget(gui);
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> list)
    {
        if(hasFlag(FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !gui.isMouseOver(this))
        {
            return;
        }

        setOffset(true);

        for(IWidget w : getWidgets())
        {
            if(w.isEnabled(gui) && gui.isMouseOver(w))
            {
                w.addMouseOverText(gui, list);
            }
        }

        setOffset(false);
    }

    @Override
    public void mousePressed(IGui gui, IMouseButton button)
    {
        if(hasFlag(FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !gui.isMouseOver(this))
        {
            return;
        }

        setOffset(true);

        for(IWidget w : getWidgets())
        {
            if(w.isEnabled(gui))
            {
                w.mousePressed(gui, button);
            }
        }

        setOffset(false);
    }

    @Override
    public void mouseReleased(IGui gui)
    {
        setOffset(true);

        for(IWidget w : getWidgets())
        {
            if(w.isEnabled(gui))
            {
                w.mouseReleased(gui);
            }
        }

        setOffset(false);
    }

    @Override
    public boolean keyPressed(IGui gui, int key, char keyChar)
    {
        setOffset(true);

        for(IWidget w : getWidgets())
        {
            if(w.isEnabled(gui) && w.keyPressed(gui, key, keyChar))
            {
                setOffset(false);
                return true;
            }
        }

        setOffset(false);
        return false;
    }
}