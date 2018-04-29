package com.feed_the_beast.ftblib.events.client;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import net.minecraft.command.ICommand;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * @author LatvianModder
 */
public class RegisterFTBClientCommandsEvent extends FTBLibEvent
{
	private final CommandTreeBase command;

	public RegisterFTBClientCommandsEvent(CommandTreeBase c)
	{
		command = c;
	}

	public void add(ICommand cmd)
	{
		command.addSubcommand(cmd);
	}
}