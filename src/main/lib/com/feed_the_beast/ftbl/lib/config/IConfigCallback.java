package com.feed_the_beast.ftbl.lib.config;

import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IConfigCallback
{
	IConfigCallback DEFAULT = (tree, sender, nbt, json) -> tree.fromJson(json);

	void saveConfig(ConfigTree tree, ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json);
}