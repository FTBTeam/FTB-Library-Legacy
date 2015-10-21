package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.mod.FTBLibFinals;

public class FTBLibNetHandler
{
	public static final SimpleNetworkWrapper NET = new SimpleNetworkWrapper(FTBLibFinals.MOD_ID);
	
	public static void init()
	{
		NET.registerMessage(MessageSendWorldID.class, MessageSendWorldID.class, 1, Side.CLIENT);
		NET.registerMessage(MessageSendGameMode.class, MessageSendGameMode.class, 2, Side.CLIENT);
		NET.registerMessage(MessageSyncConfig.class, MessageSyncConfig.class, 3, Side.CLIENT);
		NET.registerMessage(MessageReload.class, MessageReload.class, 4, Side.CLIENT);
	}
}