package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Panel extends Widget
{
    public static final int FLAG_ONLY_RENDER_WIDGETS_INSIDE = 1;
    public static final int FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE = 2;
    public static final int FLAG_UNICODE_FONT = 4;
    public static final int FLAG_DEFAULTS = FLAG_ONLY_RENDER_WIDGETS_INSIDE | FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE;

    private final List<Widget> widgets;
    private int scrollX = 0, scrollY = 0;
    private int offsetX = 0, offsetY = 0;
    private int flags = 0;

    public Panel(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        widgets = new ArrayList<>();
    }

    public void addFlags(int f)
    {
        flags |= f;
    }

    public boolean hasFlag(int flag)
    {
        return (flags & flag) != 0;
    }

    public Collection<Widget> getWidgets()
    {
        return widgets;
    }

    public abstract void addWidgets();

    public void refreshWidgets()
    {
        getWidgets().clear();
        addWidgets();
        updateWidgetPositions();
    }

    public void add(Widget widget)
    {
        widget.setParentPanel(this);
        getWidgets().add(widget);

        if(widget instanceof Panel)
        {
            ((Panel) widget).refreshWidgets();
        }
    }

    public void addAll(Widget... widgets)
    {
        for(Widget w : widgets)
        {
            add(w);
        }
    }

    public void addAll(Iterable<? extends Widget> list)
    {
        for(Widget w : list)
        {
            add(w);
        }
    }

    public void updateWidgetPositions()
    {
    }

    public void setScrollX(double scroll, int elementsWidth)
    {
        int width = getWidth();
        if(elementsWidth < width)
        {
            setScrollX(0);
        }
        else
        {
            setScrollX((int) (scroll * (elementsWidth - width)));
        }
    }

    public void setScrollY(double scroll, int elementsHeight)
    {
        int height = getHeight();
        if(elementsHeight < height)
        {
            setScrollY(0);
        }
        else
        {
            setScrollY((int) (scroll * (elementsHeight - height)));
        }
    }

    protected int alignWidgets(EnumDirection direction)
    {
        return alignWidgets(direction, 0, 0, 0);
    }

    protected int alignWidgets(EnumDirection direction, int pre, int spacing, int post)
    {
        int i = pre;

        for(Widget widget : getWidgets())
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

    public void setScrollX(int scroll)
    {
        scrollX = scroll;
    }

    public void setScrollY(int scroll)
    {
        scrollY = scroll;
    }

    @Override
    public void renderWidget(GuiBase gui)
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

        for(Widget widget : getWidgets())
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

    protected void renderPanelBackground(GuiBase gui, int ax, int ay, int w, int h)
    {
    }

    protected void renderWidget(GuiBase gui, Widget widget, int ax, int ay, int w, int h)
    {
        widget.renderWidget(gui);
    }

    @Override
    public void addMouseOverText(GuiBase gui, List<String> list)
    {
        if(hasFlag(FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !gui.isMouseOver(this))
        {
            return;
        }

        setOffset(true);

        for(Widget w : getWidgets())
        {
            if(w.isEnabled(gui) && gui.isMouseOver(w))
            {
                w.addMouseOverText(gui, list);
            }
        }

        setOffset(false);
    }

    @Override
    public void mousePressed(GuiBase gui, IMouseButton button)
    {
        if(hasFlag(FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !gui.isMouseOver(this))
        {
            return;
        }

        setOffset(true);

        for(Widget w : getWidgets())
        {
            if(w.isEnabled(gui))
            {
                w.mousePressed(gui, button);
            }
        }

        setOffset(false);
    }

    @Override
    public void mouseReleased(GuiBase gui)
    {
        setOffset(true);

        for(Widget w : getWidgets())
        {
            if(w.isEnabled(gui))
            {
                w.mouseReleased(gui);
            }
        }

        setOffset(false);
    }

    @Override
    public boolean keyPressed(GuiBase gui, int key, char keyChar)
    {
        setOffset(true);

        for(Widget w : getWidgets())
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