package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;

import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends Widget
{
    public static final int FLAG_ONLY_RENDER_WIDGETS_INSIDE = 1;
    public static final int FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE = 2;
    public static final int FLAG_UNICODE_FONT = 4;
    public static final int FLAG_DEFAULTS = FLAG_ONLY_RENDER_WIDGETS_INSIDE | FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE;

    public final List<Widget> widgets;
    private int scrollX = 0, scrollY = 0;
    private int offsetX = 0, offsetY = 0;
    private int flags = 0;

    public Panel(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        widgets = new ArrayList<>();
    }

    public Panel()
    {
        this(0, 0, 0, 0);
    }

    public void addFlags(int f)
    {
        flags |= f;
    }

    public boolean hasFlag(int flag)
    {
        return (flags & flag) != 0;
    }

    public abstract void addWidgets();

    public void refreshWidgets()
    {
        widgets.clear();
        addWidgets();
        updateWidgetPositions();
    }

    public void add(Widget widget)
    {
        widget.setParentPanel(this);
        widgets.add(widget);

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
        if(elementsHeight < height)
        {
            setScrollY(0);
        }
        else
        {
            setScrollY((int) (scroll * (elementsHeight - height)));
        }
    }

    protected final int align(WidgetLayout layout)
    {
        return layout.align(this);
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

    public void setOffset(boolean flag)
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

        if(renderInside)
        {
            GuiHelper.pushScissor(gui.getScreen(), ax, ay, width, height);
        }

        renderPanelBackground(gui, ax, ay);

        setOffset(true);

        for(int i = 0; i < widgets.size(); i++)
        {
            Widget widget = widgets.get(i);

            if(widget.shouldRender(gui) && (!renderInside || widget.collidesWith(ax, ay, width, height)))
            {
                renderWidget(gui, widget, i, ax + offsetX, ay + offsetY, width, height);
            }
        }

        setOffset(false);

        if(renderInside)
        {
            GuiHelper.popScissor();
        }

        gui.getFont().setUnicodeFlag(unicode);
    }

    protected void renderPanelBackground(GuiBase gui, int ax, int ay)
    {
        getIcon(gui).draw(ax, ay, width, height, Color4I.NONE);
    }

    protected void renderWidget(GuiBase gui, Widget widget, int index, int ax, int ay, int w, int h)
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

        for(Widget w : widgets)
        {
            if(w.isEnabled(gui) && gui.isMouseOver(w))
            {
                w.addMouseOverText(gui, list);
            }
        }

        setOffset(false);
    }

    @Override
    public boolean mousePressed(GuiBase gui, IMouseButton button)
    {
        if(hasFlag(FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !gui.isMouseOver(this))
        {
            return false;
        }

        setOffset(true);

        for(Widget w : widgets)
        {
            if(w.isEnabled(gui))
            {
                if(w.mousePressed(gui, button))
                {
                    setOffset(false);
                    return true;
                }
            }
        }

        setOffset(false);
        return false;
    }

    @Override
    public void mouseReleased(GuiBase gui)
    {
        setOffset(true);

        for(Widget w : widgets)
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

        for(Widget w : widgets)
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