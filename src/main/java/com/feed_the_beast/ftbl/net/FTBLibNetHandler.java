package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;

public class FTBLibNetHandler
{
	public static final LMNetworkWrapper NET = LMNetworkWrapper.newWrapper("FTBL");
	
	public static void init()
	{
		NET.register(1, new MessageReload());
		NET.register(2, new MessageEditConfig());
		NET.register(3, new MessageEditConfigResponse());
		NET.register(4, new MessageModifyFriends());
		NET.register(5, new MessageLMPlayerUpdate());
		NET.register(6, new MessageLMPlayerLoggedIn());
		NET.register(7, new MessageLMPlayerLoggedOut());
		NET.register(8, new MessageLMPlayerDied());
		NET.register(9, new MessageRequestSelfUpdate());
		NET.register(10, new MessageMarkTileDirty());
		NET.register(11, new MessageOpenGui());
		NET.register(12, new MessageOpenGuiTile());
		NET.register(13, new MessageClientTileAction());
		NET.register(14, new MessageClientItemAction());
		NET.register(15, new MessageNotifyPlayer());
		NET.register(16, new MessageLMPlayerInfo());
		NET.register(17, new MessageRequestPlayerInfo());
		NET.register(18, new MessageDisplayInfo());
	}
}