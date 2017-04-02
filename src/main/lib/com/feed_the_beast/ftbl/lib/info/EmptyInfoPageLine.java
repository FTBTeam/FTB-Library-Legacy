package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public abstract class EmptyInfoPageLine implements IInfoTextLine
{
    @Override
    public String getUnformattedText()
    {
        return "";
    }

    @Override
    public JsonElement getJson()
    {
        return JsonNull.INSTANCE;
    }
}