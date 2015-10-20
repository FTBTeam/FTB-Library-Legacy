package ftb.lib.mod;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class FTBLibNetHandler
{
	public static final SimpleNetworkWrapper NET = new SimpleNetworkWrapper(FTBLibFinals.MOD_ID);
	
	public static void init()
	{
		NET.registerMessage(MessageSetMode.class, MessageSetMode.class, 1, Side.CLIENT);
	}
}