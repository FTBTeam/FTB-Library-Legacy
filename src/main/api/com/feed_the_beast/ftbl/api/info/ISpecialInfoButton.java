package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.gui.IClickable;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;

/**
 * Created by LatvianModder on 02.09.2016.
 */
public interface ISpecialInfoButton extends IClickable
{
    String getTitle();

    void render(GuiBase gui, int ax, int ay);
}