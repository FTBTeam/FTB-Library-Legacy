package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IConfigContainer
{
	IConfigTree getConfigTree();

	ITextComponent getTitle();

	void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json);
}