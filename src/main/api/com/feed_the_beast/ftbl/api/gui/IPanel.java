package com.feed_the_beast.ftbl.api.gui;

import java.util.Collection;
import java.util.List;

/**
 * Created by LatvianModder on 04.09.2016.
 */
public interface IPanel extends IWidget
{
    default void add(IWidget w)
    {
        w.setParentPanel(this);
        getWidgets().add(w);

        if(w instanceof IPanel)
        {
            ((IPanel) w).refreshWidgets();
        }
    }

    default void addAll(Iterable<? extends IWidget> l)
    {
        for(IWidget w : l)
        {
            add(w);
        }
    }

    Collection<IWidget> getWidgets();

    void refreshWidgets();

    @Override
    default void addMouseOverText(IGui gui, List<String> l)
    {
        for(IWidget w : getWidgets())
        {
            if(w.isEnabled() && gui.isMouseOver(w))
            {
                w.addMouseOverText(gui, l);
            }
        }
    }

    @Override
    default void mousePressed(IGui gui, IMouseButton button)
    {
        for(IWidget w : getWidgets())
        {
            if(w.isEnabled())
            {
                w.mousePressed(gui, button);
            }
        }
    }

    @Override
    default void mouseReleased(IGui gui)
    {
        for(IWidget w : getWidgets())
        {
            if(w.isEnabled())
            {
                w.mouseReleased(gui);
            }
        }
    }

    @Override
    default boolean keyPressed(IGui gui, int key, char keyChar)
    {
        for(IWidget w : getWidgets())
        {
            if(w.isEnabled() && w.keyPressed(gui, key, keyChar))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    default void renderWidget(IGui gui)
    {
        for(IWidget widget : getWidgets())
        {
            if(widget.shouldRender(gui))
            {
                widget.renderWidget(gui);
            }
        }
    }
}
