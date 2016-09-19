package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 17.09.2016.
 */
public interface IConfigManager
{
    ISyncedRegistry<String, IConfigValueProvider> configValues();

    IRegistry<String, IConfigFileProvider> fileProviders();

    IConfigTree clientConfig();

    Collection<IConfigFile> registredFiles();

    Map<UUID, IConfigContainer> getTempConfig();
}
