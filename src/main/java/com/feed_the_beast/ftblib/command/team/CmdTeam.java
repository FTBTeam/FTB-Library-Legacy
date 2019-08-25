package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.lib.command.CmdTreeBase;
import com.feed_the_beast.ftblib.lib.command.CmdTreeHelp;

/**
 * @author LatvianModder
 */
public class CmdTeam extends CmdTreeBase
{
	public CmdTeam()
	{
		super("team");
		addSubcommand(new CmdSettings());
		addSubcommand(new CmdCreate());
		addSubcommand(new CmdLeave());
		addSubcommand(new CmdTransferOwnership());
		addSubcommand(new CmdKick());
		addSubcommand(new CmdJoin());
		addSubcommand(new CmdStatus());
		addSubcommand(new CmdRequestInvite());
		addSubcommand(new CmdDelete());
		addSubcommand(new CmdCreateServerTeam());
		addSubcommand(new CmdInfo());
		addSubcommand(new CmdGet());
		addSubcommand(new CmdList());
		addSubcommand(new CmdSettingsFor());
		addSubcommand(new CmdTreeHelp(this));
	}
}