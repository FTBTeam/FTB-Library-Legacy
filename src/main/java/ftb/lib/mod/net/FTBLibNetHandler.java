package ftb.lib.mod.net;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.LMNetworkWrapper;

public class FTBLibNetHandler
{
	static final LMNetworkWrapper NET = LMNetworkWrapper.newWrapper("FTBL");
	
	public static void init()
	{
		NET.register(MessageSendWorldID.class, 1, Side.CLIENT);
		NET.register(MessageSendGameMode.class, 2, Side.CLIENT);
		NET.register(MessageSyncConfig.class, 3, Side.CLIENT);
		NET.register(MessageReload.class, 4, Side.CLIENT);
	}
}