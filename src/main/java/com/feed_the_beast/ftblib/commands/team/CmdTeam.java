package com.feed_the_beast.ftblib.commands.team;

import com.feed_the_beast.ftblib.lib.cmd.CmdTreeBase;

/**
 * @author LatvianModder
 */
public class CmdTeam extends CmdTreeBase
{
	public CmdTeam()
	{
		super("team");
		addSubcommand(new CmdTeamConfig());
		addSubcommand(new CmdCreate());
		addSubcommand(new CmdLeave());
		addSubcommand(new CmdTransferOwnership());
		addSubcommand(new CmdKick());
		addSubcommand(new CmdJoin());
		addSubcommand(new CmdSetStatus());
		addSubcommand(new CmdRequestInvite());
	}
}