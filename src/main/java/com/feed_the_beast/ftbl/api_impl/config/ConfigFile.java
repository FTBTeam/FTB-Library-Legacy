package com.feed_the_beast.ftbl.api_impl.config;

import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.google.gson.JsonElement;
import com.latmod.lib.util.LMJsonUtils;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class ConfigFile extends ConfigTree implements IConfigFile
{
    private File file;

    @Nullable
    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public void setFile(File f)
    {
        file = f;
    }

    @Override
    public void load()
    {
        JsonElement e = LMJsonUtils.fromJson(getFile());

        if(e.isJsonObject())
        {
            fromJson(e.getAsJsonObject());
        }
    }

    @Override
    public void save()
    {
        File f = getFile();

        if(f != null)
        {
            LMJsonUtils.toJson(f, getSerializableElement());
        }
    }
}