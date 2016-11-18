package com.feed_the_beast.ftbl.api.info;

import com.google.gson.JsonObject;

/**
 * Created by LatvianModder on 02.10.2016.
 */
public interface IInfoTextLineProvider
{
    IInfoTextLine create(IGuiInfoPage page, JsonObject json);
}