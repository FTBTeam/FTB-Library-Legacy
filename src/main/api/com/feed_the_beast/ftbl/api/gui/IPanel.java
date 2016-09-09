package com.feed_the_beast.ftbl.api.gui;

/**
 * Created by LatvianModder on 04.09.2016.
 */
public interface IPanel extends IWidget
{
    void add(IWidget w);

    default void addAll(Iterable<? extends IWidget> l)
    {
        for(IWidget w : l)
        {
            add(w);
        }
    }

    void refreshWidgets();
}
