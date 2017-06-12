package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.events.RegisterFTBCommandsEvent;
import com.feed_the_beast.ftbl.cmd.team.CmdTeam;
import com.feed_the_beast.ftbl.lib.cmd.CmdTreeBase;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author LatvianModder
 */
public class CmdFTB extends CmdTreeBase
{
	public CmdFTB(boolean dedi)
	{
		super("ftb");
		addSubcommand(new CmdReload());
		addSubcommand(new CmdReloadClient());
		addSubcommand(new CmdMySettings());
		addSubcommand(new CmdTeam());
		addSubcommand(new CmdPackMode());
		addSubcommand(new CmdNotify());
		addSubcommand(new CmdEditGamerules());
		addSubcommand(new CmdEditConfig());

		if (LMUtils.DEV_ENV)
		{
			addSubcommand(new CmdAddFakePlayer());
		}

		MinecraftForge.EVENT_BUS.post(new RegisterFTBCommandsEvent(this, dedi));
		setCustomPermissionPrefix("");
	}
}