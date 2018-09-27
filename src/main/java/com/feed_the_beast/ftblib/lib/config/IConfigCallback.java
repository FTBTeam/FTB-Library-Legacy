package com.feed_the_beast.ftblib.lib.config;

import net.minecraft.command.ICommandSender;

/**
 * @author LatvianModder
 */
@FunctionalInterface
public interface IConfigCallback
{
	IConfigCallback DEFAULT = (group, sender) -> {};

	void onConfigSaved(ConfigGroup group, ICommandSender sender);
}