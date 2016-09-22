package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 07.06.2016.
 */
public interface IConfigContainer
{
    IConfigTree getTree();

    ITextComponent getTitle();

    void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json);
}