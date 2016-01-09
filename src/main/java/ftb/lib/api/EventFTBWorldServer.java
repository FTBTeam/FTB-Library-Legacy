package ftb.lib.api;

import ftb.lib.FTBWorld;
import net.minecraft.server.MinecraftServer;

public class EventFTBWorldServer extends EventLM
{
	public final FTBWorld world;
	public final MinecraftServer server;
	
	public EventFTBWorldServer(FTBWorld w, MinecraftServer s)
	{
		world = w;
		server = s;
	}
}