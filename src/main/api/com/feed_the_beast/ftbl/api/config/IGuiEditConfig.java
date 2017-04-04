package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IGuiEditConfig
{
    void onChanged(IConfigKey key, JsonElement json);

    void openGui();
}