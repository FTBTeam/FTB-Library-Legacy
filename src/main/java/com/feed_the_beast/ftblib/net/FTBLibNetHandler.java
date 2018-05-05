package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;

/**
 * @author LatvianModder
 */
public class FTBLibNetHandler
{
	static final NetworkWrapper GENERAL = NetworkWrapper.newWrapper(FTBLib.MOD_ID);
	static final NetworkWrapper EDIT_CONFIG = NetworkWrapper.newWrapper("ftblib_edit_config");
	static final NetworkWrapper MY_TEAM = NetworkWrapper.newWrapper("ftblib_my_team");

	public static void init()
	{
		GENERAL.register(new MessageSyncData());
		GENERAL.register(new MessageOpenGui());
		GENERAL.register(new MessageCloseGui());
		GENERAL.register(new MessageAdminPanelGui());
		GENERAL.register(new MessageAdminPanelGuiResponse());
		GENERAL.register(new MessageAdminPanelAction());

		EDIT_CONFIG.register(new MessageEditConfig());
		EDIT_CONFIG.register(new MessageEditConfigResponse());

		MY_TEAM.register(new MessageSelectTeamGui());
		MY_TEAM.register(new MessageMyTeamGui());
		MY_TEAM.register(new MessageMyTeamGuiResponse());
		MY_TEAM.register(new MessageMyTeamAction());
		MY_TEAM.register(new MessageMyTeamPlayerList());
	}
}