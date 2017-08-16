package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.events.RegisterFTBCommandsEvent;
import com.feed_the_beast.ftbl.cmd.team.CmdTeam;
import com.feed_the_beast.ftbl.lib.cmd.CmdTreeBase;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;

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
		addSubcommand(new CmdEditConfig());

		if (CommonUtils.DEV_ENV)
		{
			addSubcommand(new CmdAddFakePlayer());
		}

		new RegisterFTBCommandsEvent(this, dedi).post();
		setCustomPermissionPrefix("");
	}
}