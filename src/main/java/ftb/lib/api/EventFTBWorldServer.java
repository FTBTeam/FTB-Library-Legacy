package ftb.lib.api;

import ftb.lib.FTBWorld;

public class EventFTBWorldServer extends EventLM
{
	public final FTBWorld world;
	
	public EventFTBWorldServer(FTBWorld w)
	{ world = w; }
}