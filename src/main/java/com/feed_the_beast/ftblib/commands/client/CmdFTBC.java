package com.feed_the_beast.ftblib.commands.client;

import com.feed_the_beast.ftblib.events.client.RegisterFTBClientCommandsEvent;
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
	}
}