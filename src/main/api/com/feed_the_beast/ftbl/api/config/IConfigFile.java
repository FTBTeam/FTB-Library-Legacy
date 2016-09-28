package com.feed_the_beast.ftbl.api.config;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IConfigFile extends IConfigTree, IConfigContainer
{
    void load();

    void save();
}