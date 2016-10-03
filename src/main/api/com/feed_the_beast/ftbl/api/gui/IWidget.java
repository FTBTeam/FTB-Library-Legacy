package com.feed_the_beast.ftbl.api.gui;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 04.09.2016.
 */
public interface IWidget
{
    @Nullable
    IPanel getParentPanel();

    void setParentPanel(@Nullable IPanel p);

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setX(int v);

    void setY(int v);

    void setWidth(int v);

    void setHeight(int v);

    default int getAX()
    {
        return (getParentPanel() == null) ? getX() : (getParentPanel().getAX() + getX());
    }

    default int getAY()
    {
        return (getParentPanel() == null) ? getY() : (getParentPanel().getAY() + getY());
    }

    default boolean isInside(IWidget w)
    {
        double a0 = getAY();
        double a1 = w.getAY();

        if(a1 + w.getHeight() >= a0 || a1 <= a0 + getHeight())
        {
            return true;
        }

        a0 = getAX();
        a1 = w.getAX();

        return (a1 + w.getWidth() < a0 && a1 > a0 + getWidth());
    }

    default boolean isEnabled()
    {
        return true;
    }

    default boolean shouldRender(IGui gui)
    {
        return gui.isInside(this);
    }

    default void mousePressed(IGui gui, IMouseButton button)
    {
    }

    default void mouseReleased(IGui gui)
    {
    }

    default boolean keyPressed(IGui gui, int key, char keyChar)
    {
        return false;
    }

    default void addMouseOverText(IGui gui, List<String> l)
    {
    }

    default void renderWidget(IGui gui)
    {
    }
}