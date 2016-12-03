package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;

/**
 * Created by LatvianModder on 02.12.2016.
 */
public interface IRankConfig extends IConfigKey
{
    IConfigValue getDefOPValue();
}