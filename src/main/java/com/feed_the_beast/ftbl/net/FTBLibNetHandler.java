package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;

/**
 * @author LatvianModder
 */
public class FTBLibNetHandler
{
	static final NetworkWrapper NET = NetworkWrapper.newWrapper(FTBLibFinals.MOD_ID);

	public static void init()
	{
		NET.register(1, new MessageSyncData());
		NET.register(2, new MessageEditConfig());
		NET.register(3, new MessageEditConfigResponse());
		NET.register(4, new MessageOpenGui());
		NET.register(5, new MessageCloseGui());
		//6
		//7
		//8
		NET.register(9, new MessageSelectTeamGui());
		NET.register(10, new MessageMyTeamGui());
		NET.register(11, new MessageMyTeamAddPlayerGui());
	}
}