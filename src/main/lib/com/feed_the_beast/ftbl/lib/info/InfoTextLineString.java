package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public class InfoTextLineString extends EmptyInfoPageLine
{
    private final String text;

    public InfoTextLineString(String t)
    {
        text = t;
    }

    public InfoTextLineString(JsonElement e)
    {
        text = e.getAsString();
    }

    @Override
    public String getUnformattedText()
    {
        return text;
    }

    @Override
    public IWidget createWidget(IGui gui, IPanel parent)
    {
        return new ButtonInfoTextLine(gui, parent, text);
    }

    @Override
    public JsonElement getJson()
    {
        return new JsonPrimitive(text);
    }

    @Override
    public IInfoTextLine copy(InfoPage page)
    {
        return new InfoTextLineString(text);
    }
}