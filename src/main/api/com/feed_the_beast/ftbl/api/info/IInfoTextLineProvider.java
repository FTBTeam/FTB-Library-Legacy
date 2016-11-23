package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.google.gson.JsonObject;

/**
 * Created by LatvianModder on 02.10.2016.
 */
public interface IInfoTextLineProvider
{
    IInfoTextLine create(InfoPage page, JsonObject json);
}