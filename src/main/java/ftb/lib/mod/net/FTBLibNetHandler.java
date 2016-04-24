package ftb.lib.mod.net;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.net.LMNetworkWrapper;

public class FTBLibNetHandler
{
	static final LMNetworkWrapper NET = LMNetworkWrapper.newWrapper("FTBL");
	
	public static void init()
	{
		NET.register(MessageReload.class, 1, Side.CLIENT);
		NET.register(MessageEditConfig.class, 2, Side.CLIENT);
		NET.register(MessageEditConfigResponse.class, 3, Side.SERVER);
		NET.register(MessageOpenGui.class, 4, Side.CLIENT);
		NET.register(MessageOpenGuiTile.class, 5, Side.CLIENT);
		NET.register(MessageClientTileAction.class, 6, Side.SERVER);
		NET.register(MessageClientItemAction.class, 7, Side.SERVER);
		NET.register(MessageNotifyPlayer.class, 8, Side.CLIENT);
		NET.register(MessageDisplayInfo.class, 9, Side.CLIENT);
	}
}