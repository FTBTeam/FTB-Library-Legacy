package ftb.lib.mod.net;

import ftb.lib.api.net.LMNetworkWrapper;

public class FTBLibNetHandler
{
	public static final LMNetworkWrapper NET = LMNetworkWrapper.newWrapper("FTBL");
	public static final LMNetworkWrapper NET_INFO = LMNetworkWrapper.newWrapper("FTBLI");
	
	public static void init()
	{
		new MessageReload().register(1);
		new MessageEditConfig().register(2);
		new MessageEditConfigResponse().register(3);
		new MessageModifyFriends().register(4);
		new MessageLMPlayerUpdate().register(5);
		new MessageLMPlayerLoggedIn().register(6);
		new MessageLMPlayerLoggedOut().register(7);
		new MessageLMPlayerDied().register(8);
		new MessageRequestSelfUpdate().register(9);
		new MessageMarkTileDirty().register(10);
		
		new MessageOpenGui().register(1);
		new MessageOpenGuiTile().register(2);
		new MessageClientTileAction().register(3);
		new MessageClientItemAction().register(4);
		new MessageNotifyPlayer().register(5);
		new MessageLMPlayerInfo().register(6);
		new MessageRequestPlayerInfo().register(7);
		new MessageDisplayInfo().register(8);
	}
}