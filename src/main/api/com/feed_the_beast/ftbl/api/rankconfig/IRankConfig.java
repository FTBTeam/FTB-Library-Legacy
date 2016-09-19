package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import net.minecraft.util.IStringSerializable;

/**
 * Created by LatvianModder on 15.09.2016.
 */
public interface IRankConfig extends IStringSerializable
{
    IConfigValue getDefaultValue();

    IConfigValue getDefaultOPValue();

    String getDescription();
}