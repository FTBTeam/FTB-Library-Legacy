package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.events.RegisterFTBClientCommandsEvent;
import com.feed_the_beast.ftbl.cmd.client.CmdClientConfig;
import com.feed_the_beast.ftbl.lib.cmd.CmdTreeBase;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author LatvianModder
 */
public class CmdFTBC extends CmdTreeBase
{
	public CmdFTBC()
	{
		super("ftbc");
		addSubcommand(new CmdClientConfig());

		MinecraftForge.EVENT_BUS.post(new RegisterFTBClientCommandsEvent(this));
		setCustomPermissionPrefix("");
	}
}