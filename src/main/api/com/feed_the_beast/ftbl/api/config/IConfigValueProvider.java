package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.IRegistryObject;

/**
 * Created by LatvianModder on 17.09.2016.
 */
public interface IConfigValueProvider extends IRegistryObject
{
    IConfigValue createConfigValue();
}