package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;

/**
 * @author LatvianModder
 */
public class FTBLibNetHandler
{
	static final NetworkWrapper GENERAL = NetworkWrapper.newWrapper(FTBLibFinals.MOD_ID);
	static final NetworkWrapper EDIT_CONFIG = NetworkWrapper.newWrapper(FTBLibFinals.MOD_ID + "_edit_config");
	static final NetworkWrapper MY_TEAM = NetworkWrapper.newWrapper(FTBLibFinals.MOD_ID + "_my_team");

	public static void init()
	{
		GENERAL.register(1, new MessageSyncData());
		GENERAL.register(2, new MessageOpenGui());
		GENERAL.register(3, new MessageCloseGui());

		EDIT_CONFIG.register(1, new MessageEditConfig());
		EDIT_CONFIG.register(2, new MessageEditConfigResponse());

		MY_TEAM.register(1, new MessageSelectTeamGui());
		MY_TEAM.register(2, new MessageMyTeamGui());
		MY_TEAM.register(3, new MessageMyTeamAction());
		MY_TEAM.register(4, new MessageMyTeamPlayerList());
	}
}