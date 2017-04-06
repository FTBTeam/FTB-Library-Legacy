package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;

/**
 * @author LatvianModder
 */
public interface ISpecialInfoButton
{
    String getTitle(GuiBase gui);

    void onClicked(GuiBase gui, IMouseButton button);

    void draw(GuiBase gui, int x, int y, int w, int h);
}