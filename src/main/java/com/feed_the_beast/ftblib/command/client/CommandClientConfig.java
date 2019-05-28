package com.feed_the_beast.ftblib.command.client;

import com.feed_the_beast.ftblib.client.GuiClientConfig;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CommandClientConfig extends CmdBase
{
	public CommandClientConfig()
	{
		super("client_config", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		new GuiClientConfig().openGuiLater();
	}
}