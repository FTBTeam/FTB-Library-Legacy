package com.feed_the_beast.ftbl.lib.config;

import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;

/**
 * @author LatvianModder
 */
public interface IConfigCallback
{
	IConfigCallback DEFAULT = (group, sender, json) -> group.fromJson(json);

	void saveConfig(ConfigGroup group, ICommandSender sender, JsonObject json);
}