package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;

/**
 * Created by LatvianModder on 15.09.2016.
 */
public interface IRankConfig extends IConfigKey
{
    IConfigValue getDefOPValue();
}