package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.google.gson.JsonElement;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoTextLine
{
    String getUnformattedText();

    Widget createWidget(GuiBase gui, Panel parent);

    IInfoTextLine copy(InfoPage page);

    JsonElement getJson();

    default boolean isEmpty()
    {
        return getUnformattedText().isEmpty();
    }
}