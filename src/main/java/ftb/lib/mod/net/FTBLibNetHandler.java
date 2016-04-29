package ftb.lib.mod.net;

import ftb.lib.api.net.LMNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class FTBLibNetHandler
{
	public static final LMNetworkWrapper NET = LMNetworkWrapper.newWrapper("FTBL");
	public static final LMNetworkWrapper NET_INFO = LMNetworkWrapper.newWrapper("FTBLI");
	
	public static void init()
	{
		NET.register(MessageReload.class, 1, Side.CLIENT);
		NET.register(MessageEditConfig.class, 2, Side.CLIENT);
		NET.register(MessageEditConfigResponse.class, 3, Side.SERVER);
		NET.register(MessageModifyFriends.class, 4, Side.SERVER);
		NET.register(MessageLMPlayerUpdate.class, 5, Side.CLIENT);
		NET.register(MessageLMPlayerLoggedIn.class, 6, Side.CLIENT);
		NET.register(MessageLMPlayerLoggedOut.class, 7, Side.CLIENT);
		NET.register(MessageLMPlayerDied.class, 8, Side.CLIENT);
		NET.register(MessageRequestSelfUpdate.class, 9, Side.SERVER);
		NET.register(MessageMarkTileDirty.class, 10, Side.CLIENT);
		
		NET_INFO.register(MessageOpenGui.class, 1, Side.CLIENT);
		NET_INFO.register(MessageOpenGuiTile.class, 2, Side.CLIENT);
		NET_INFO.register(MessageClientTileAction.class, 3, Side.SERVER);
		NET_INFO.register(MessageClientItemAction.class, 4, Side.SERVER);
		NET_INFO.register(MessageNotifyPlayer.class, 5, Side.CLIENT);
		NET_INFO.register(MessageLMPlayerInfo.class, 6, Side.CLIENT);
		NET_INFO.register(MessageRequestPlayerInfo.class, 7, Side.SERVER);
		NET_INFO.register(MessageDisplayInfo.class, 8, Side.CLIENT);
	}
}