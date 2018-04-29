package com.feed_the_beast.ftblib.commands;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.commands.team.CmdTeam;
import com.feed_the_beast.ftblib.events.RegisterFTBCommandsEvent;
import com.feed_the_beast.ftblib.lib.cmd.CmdTreeBase;

/**
 * @author LatvianModder
 */
public class CmdFTB extends CmdTreeBase
{
	public CmdFTB(boolean dedi)
	{
		super("ftb");
		addSubcommand(new CmdReload());
		addSubcommand(new CmdMySettings());
		addSubcommand(new CmdTeam());
		addSubcommand(new CmdNotify());

		if (FTBLibConfig.debugging.special_commands)
		{
			addSubcommand(new CmdAddFakePlayer());
		}

		new RegisterFTBCommandsEvent(this, dedi).post();
	}
}