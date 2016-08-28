package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.latmod.lib.util.LMFileUtils;
import com.latmod.lib.util.LMJsonUtils;

import javax.annotation.Nullable;
import java.io.File;

public class ConfigFile extends ConfigGroup
{
    private File file;

    public ConfigFile()
    {
    }

    @Override
    public ConfigFile asConfigFile()
    {
        return this;
    }

    @Nullable
    public File getFile()
    {
        return file;
    }

    public void setFile(File f)
    {
        file = LMFileUtils.newFile(f);
    }

    public void load()
    {
        JsonElement e = LMJsonUtils.fromJson(getFile());

        if(e.isJsonObject())
        {
            loadFromGroup(e.getAsJsonObject());
        }
    }

    public void save()
    {
        File f = getFile();

        if(f != null)
        {
            LMJsonUtils.toJson(f, getSerializableElement());
        }
    }

    public void addGroup(String id, Class<?> c)
    {
        add(id, new ConfigGroup().addAll(c, null));
    }
}