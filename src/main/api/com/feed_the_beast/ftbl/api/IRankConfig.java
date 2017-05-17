package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;

/**
 * @author LatvianModder
 */
public interface IRankConfig extends IConfigKey
{
    IConfigValue getDefOPValue();
}