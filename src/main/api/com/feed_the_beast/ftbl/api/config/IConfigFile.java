package com.feed_the_beast.ftbl.api.config;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IConfigFile extends IConfigTree
{
    @Nullable
    File getFile();

    void setFile(File f);

    void load();

    void save();
}
