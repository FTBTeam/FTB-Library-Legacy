package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import net.minecraft.command.ICommand;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * @author LatvianModder
 */
public class RegisterFTBCommandsEvent extends FTBLibEvent
{
	private final CommandTreeBase command;
	private final boolean dedi;

	public RegisterFTBCommandsEvent(CommandTreeBase c, boolean d)
	{
		command = c;
		dedi = d;
	}

	public void add(ICommand cmd)
	{
		command.addSubcommand(cmd);
	}

	public void add(ICommand cmd, ConfigValue value)
	{
		if (value.getBoolean())
		{
			add(cmd);
		}
	}

	public boolean isDedicatedServer()
	{
		return dedi;
	}
}