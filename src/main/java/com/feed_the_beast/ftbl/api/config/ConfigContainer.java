package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.ResourceLocationObject;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by LatvianModder on 07.06.2016.
 */
public abstract class ConfigContainer extends ResourceLocationObject
{
    public ConfigContainer(ResourceLocation id)
    {
        super(id);
    }

    public abstract ConfigGroup createGroup();

    public abstract ITextComponent getConfigTitle();

    public abstract void saveConfig(ICommandSender sender, NBTTagCompound nbt, JsonObject json);
}