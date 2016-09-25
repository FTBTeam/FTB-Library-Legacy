package com.feed_the_beast.ftbl.api.config;

import java.io.File;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IConfigFile extends IConfigTree, IConfigContainer
{
    File getFile();

    void load();

    void save();
}
