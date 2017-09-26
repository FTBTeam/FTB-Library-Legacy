package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.RegisterFTBClientCommandsEvent;
import com.feed_the_beast.ftbl.cmd.client.CmdClientConfig;
import com.feed_the_beast.ftbl.lib.cmd.CmdTreeBase;

/**
 * @author LatvianModder
 */
public class CmdFTBC extends CmdTreeBase
{
	public CmdFTBC()
	{
		super("ftbc");
		addSubcommand(new CmdClientConfig());
		addSubcommand(new CmdReloadClient());

		new RegisterFTBClientCommandsEvent(this).post();
		setCustomPermissionPrefix("");
	}
}