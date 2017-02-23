package com.feed_the_beast.ftbl.api.gui;

import java.util.List;

/**
 * Created by LatvianModder on 04.09.2016.
 */
public interface IWidget
{
    IPanel getParentPanel();

    void setParentPanel(IPanel p);

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
        return getParentPanel().getAX() + getX();
    }

    default int getAY()
    {
        return getParentPanel().getAY() + getY();
    }

    default boolean collidesWith(int x, int y, int w, int h)
    {
        int ay = getAY();
        if(ay >= y + h || ay + getHeight() <= y)
        {
            return false;
        }

        int ax = getAX();
        return ax < x + w && ax + getWidth() > x;
    }

    default boolean isEnabled(IGui gui)
    {
        return true;
    }

    default boolean shouldRender(IGui gui)
    {
        return true;
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

    default void addMouseOverText(IGui gui, List<String> list)
    {
    }

    default void renderWidget(IGui gui)
    {
    }
}