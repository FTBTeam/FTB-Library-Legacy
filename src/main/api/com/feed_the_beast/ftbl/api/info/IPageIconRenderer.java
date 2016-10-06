package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IWidget;

/**
 * Created by LatvianModder on 06.10.2016.
 */
public interface IPageIconRenderer
{
    void renderIcon(IGui gui, IWidget widget, int x, int y);
}