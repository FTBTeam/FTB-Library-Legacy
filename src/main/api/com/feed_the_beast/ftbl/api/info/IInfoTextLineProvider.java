package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.IRegistryObject;
import com.google.gson.JsonObject;

/**
 * Created by LatvianModder on 02.10.2016.
 */
public interface IInfoTextLineProvider extends IRegistryObject
{
    IInfoTextLine create(IGuiInfoPage page, JsonObject json);
}