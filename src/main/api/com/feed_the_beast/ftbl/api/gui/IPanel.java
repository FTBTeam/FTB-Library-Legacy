package com.feed_the_beast.ftbl.api.gui;

import java.util.Collection;

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

    default void updateWidgetPositions()
    {
    }
}