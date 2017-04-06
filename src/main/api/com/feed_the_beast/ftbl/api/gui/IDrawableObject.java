package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 22.02.2017.
 */
public interface IDrawableObject
{
    @SideOnly(Side.CLIENT)
    void draw(int x, int y, int w, int h, Color4I col);

    @SideOnly(Side.CLIENT)
    default void draw(Widget widget, Color4I col)
    {
        draw(widget.getAX(), widget.getAY(), widget.width, widget.height, col);
    }
}