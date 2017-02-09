package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.text.ITextComponent;

import java.io.File;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class ConfigFile extends ConfigTree implements IConfigFile
{
    public static final IConfigFileProvider NULL_FILE_PROVIDER = () -> null;

    private final ITextComponent displayName;
    private final IConfigFileProvider fileProvider;

    public ConfigFile(ITextComponent n, IConfigFileProvider p)
    {
        displayName = n;
        fileProvider = p;
    }

    @Override
    public void load()
    {
        File f = fileProvider.getFile();

        if(f != null)
        {
            JsonElement e = LMJsonUtils.fromJson(f);

            if(e.isJsonObject())
            {
                fromJson(LMJsonUtils.fromJsonTree(e.getAsJsonObject()));
            }
        }
    }

    @Override
    public void save()
    {
        File f = fileProvider.getFile();

        if(f != null)
        {
            LMJsonUtils.toJson(f, LMJsonUtils.toJsonTree(getSerializableElement().getAsJsonObject().entrySet()));
        }
    }

    @Override
    public ITextComponent getTitle()
    {
        return displayName;
    }
}