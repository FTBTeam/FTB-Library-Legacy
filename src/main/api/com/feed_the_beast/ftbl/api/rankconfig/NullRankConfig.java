package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.PropertyNull;

/**
 * Created by LatvianModder on 15.09.2016.
 */
public enum NullRankConfig implements IRankConfig
{
    INSTANCE;

    @Override
    public IConfigValue getDefaultValue()
    {
        return PropertyNull.INSTANCE;
    }

    @Override
    public IConfigValue getDefaultOPValue()
    {
        return PropertyNull.INSTANCE;
    }

    @Override
    public String getDescription()
    {
        return "";
    }

    @Override
    public String getName()
    {
        return "null";
    }
}
