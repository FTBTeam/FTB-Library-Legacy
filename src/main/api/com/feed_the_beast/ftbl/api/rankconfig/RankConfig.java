package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.latmod.lib.FinalIDObject;

/**
 * Created by LatvianModder on 15.09.2016.
 */
class RankConfig extends FinalIDObject implements IRankConfig
{
    private final IConfigValue defaultValue, defaultOPValue;
    private final String desc;

    RankConfig(String s, IConfigValue def, IConfigValue defOP, String d)
    {
        super(s);
        defaultValue = def;
        defaultOPValue = def.copy(); // To ensure that defOP gets all def properties
        defaultOPValue.deserializeNBT(defOP.serializeNBT());
        desc = d;
    }

    @Override
    public IConfigValue getDefaultValue()
    {
        return defaultValue;
    }

    @Override
    public IConfigValue getDefaultOPValue()
    {
        return defaultOPValue;
    }

    @Override
    public String getDescription()
    {
        return desc;
    }
}