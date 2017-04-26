package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.TextField;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public class GuideTextLineString extends EmptyGuidePageLine
{
    private final String text;

    public GuideTextLineString(String t)
    {
        text = t;
    }

    public GuideTextLineString(JsonElement e)
    {
        text = e.getAsString();
    }

    @Override
    public String getUnformattedText()
    {
        return text;
    }

    @Override
    public Widget createWidget(GuiBase gui, Panel parent)
    {
        return new TextField(0, 0, parent.width, -1, gui.getFont(), text);
    }

    @Override
    public JsonElement getJson()
    {
        return new JsonPrimitive(text);
    }

    @Override
    public IGuideTextLine copy(GuidePage page)
    {
        return new GuideTextLineString(text);
    }
}