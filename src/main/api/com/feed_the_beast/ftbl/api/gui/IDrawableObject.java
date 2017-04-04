package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.lib.gui.Widget;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 22.02.2017.
 */
public interface IDrawableObject
{
    @SideOnly(Side.CLIENT)
    void draw(int x, int y, int w, int h);

    @SideOnly(Side.CLIENT)
    default void draw(Widget widget)
    {
        draw(widget.getAX(), widget.getAY(), widget.getWidth(), widget.getHeight());
    }
}