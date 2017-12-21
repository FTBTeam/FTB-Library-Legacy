package com.feed_the_beast.ftblib.cmd;

import com.feed_the_beast.ftblib.cmd.client.CmdClientConfig;
import com.feed_the_beast.ftblib.events.RegisterFTBClientCommandsEvent;
import com.feed_the_beast.ftblib.lib.cmd.CmdTreeBase;

/**
 * @author LatvianModder
 */
public class CmdFTBC extends CmdTreeBase
{
	public CmdFTBC()
	{
		super("ftbc");
		addSubcommand(new CmdClientConfig());

		new RegisterFTBClientCommandsEvent(this).post();
		setCustomPermissionPrefix("");
	}
}