package com.feed_the_beast.ftbl.api.config;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created by LatvianModder on 17.09.2016.
 */
public interface IConfigFileProvider
{
    @Nullable
    File getFile();
}