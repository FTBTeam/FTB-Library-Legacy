package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public class InfoTextLineString implements IInfoTextLine
{
    private String text;

    public InfoTextLineString(String s)
    {
        text = s;
    }

    @Override
    public String getUnformattedText()
    {
        return text;
    }

    @Override
    public IWidget createWidget(GuiInfo gui, InfoPage page)
    {
        return new ButtonInfoTextLine(gui, text);
    }

    @Override
    public void fromJson(JsonElement e)
    {
        text = e.getAsString();
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(text);
    }
}
