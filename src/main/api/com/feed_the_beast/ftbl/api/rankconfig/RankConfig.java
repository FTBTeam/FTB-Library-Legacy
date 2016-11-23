package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;

/**
 * Created by LatvianModder on 15.09.2016.
 */
public class RankConfig extends ConfigKey implements IRankConfig
{
    private final IConfigValue defaultOPValue;

    public RankConfig(String s, IConfigValue def, IConfigValue defOP, String... info)
    {
        super(s, def);
        defaultOPValue = def.copy();
        defaultOPValue.deserializeNBT(defOP.serializeNBT());
        setInfo(info);
    }

    @Override
    public IConfigValue getDefOPValue()
    {
        return defaultOPValue;
    }
}