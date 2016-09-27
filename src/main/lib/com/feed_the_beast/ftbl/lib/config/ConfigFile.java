package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public class ConfigFile extends ConfigTree implements IConfigFile
{
    private final ITextComponent displayName;
    private final IConfigFileProvider fileProvider;
    private File file;

    public ConfigFile(ITextComponent n, IConfigFileProvider p)
    {
        displayName = n;
        fileProvider = p;
    }

    @Override
    public File getFile()
    {
        if(file == null)
        {
            file = fileProvider.getFile();
        }

        return file;
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
        LMJsonUtils.toJson(getFile(), getSerializableElement());
    }

    @Override
    public ITextComponent getTitle()
    {
        return displayName;
    }

    @Override
    public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
    {
        fromJson(json);
        save();
    }

    @Override
    public IConfigTree getConfigTree()
    {
        return this;
    }
}