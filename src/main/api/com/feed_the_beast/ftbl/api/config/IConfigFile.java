package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IConfigFile extends IConfigTree, IConfigContainer
{
    void load();

    void save();

    @Override
    default IConfigTree getConfigTree()
    {
        return this;
    }

    @Override
    default void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
    {
        fromJson(json);
        save();
    }
}