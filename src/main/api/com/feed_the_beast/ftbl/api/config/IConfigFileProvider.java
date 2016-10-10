package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.IRegistryObject;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created by LatvianModder on 17.09.2016.
 */
public interface IConfigFileProvider extends IRegistryObject
{
    @Nullable
    File getFile();
}