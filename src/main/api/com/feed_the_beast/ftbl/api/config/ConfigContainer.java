package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by LatvianModder on 07.06.2016.
 */
public abstract class ConfigContainer
{
    public abstract ConfigGroup createGroup();

    public abstract ITextComponent getConfigTitle();

    public abstract void saveConfig(ICommandSender sender, NBTTagCompound nbt, JsonObject json);
}