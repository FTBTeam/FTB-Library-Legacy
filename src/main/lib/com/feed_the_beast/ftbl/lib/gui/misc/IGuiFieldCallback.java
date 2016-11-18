package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigValue;

/**
 * Created by LatvianModder on 15.11.2016.
 */
public interface IGuiFieldCallback
{
    void onCallback(IConfigValue value, boolean set);
}