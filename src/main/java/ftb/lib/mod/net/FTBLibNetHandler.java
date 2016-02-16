package ftb.lib.mod.net;

import ftb.lib.api.net.LMNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class FTBLibNetHandler
{
	static final LMNetworkWrapper NET = LMNetworkWrapper.newWrapper("FTBL");
	static final LMNetworkWrapper NET_GUI = LMNetworkWrapper.newWrapper("FTBLG");
	static final LMNetworkWrapper NET_INFO = LMNetworkWrapper.newWrapper("FTBLI");
	
	public static void init()
	{
		NET.register(MessageLMWorldUpdate.class, 1, Side.CLIENT);
		//NET.register(MessageSendGameMode.class, 2, Side.CLIENT);
		//NET.register(MessageSyncConfig.class, 3, Side.CLIENT);
		NET.register(MessageReload.class, 4, Side.CLIENT);
		NET.register(MessageEditConfig.class, 5, Side.CLIENT);
		NET.register(MessageEditConfigResponse.class, 6, Side.SERVER);
		
		NET_GUI.register(MessageOpenGui.class, 1, Side.CLIENT);
		NET_GUI.register(MessageOpenGuiTile.class, 2, Side.CLIENT);
		NET_GUI.register(MessageClientTileAction.class, 3, Side.SERVER);
		NET_GUI.register(MessageClientItemAction.class, 4, Side.SERVER);
		NET_GUI.register(MessageNotifyPlayer.class, 5, Side.CLIENT);
		
		NET_INFO.register(MessageLMPlayerInfo.class, 1, Side.CLIENT);
	}
}