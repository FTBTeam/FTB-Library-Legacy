package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.gui.GuiInfo;
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
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPage page)
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
