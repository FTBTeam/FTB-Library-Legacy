package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Widget;

/**
 * Created by LatvianModder on 06.10.2016.
 */
public interface IPageIconRenderer
{
    void renderIcon(GuiBase gui, Widget widget, int x, int y);
}