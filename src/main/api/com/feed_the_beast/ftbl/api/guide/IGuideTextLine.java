package com.feed_the_beast.ftbl.api.guide;

import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.google.gson.JsonElement;

/**
 * @author LatvianModder
 */
public interface IGuideTextLine
{
    String getUnformattedText();

    Widget createWidget(GuiBase gui, Panel parent);

    IGuideTextLine copy(GuidePage page);

    JsonElement getJson();

    default boolean isEmpty()
    {
        return getUnformattedText().isEmpty();
    }
}