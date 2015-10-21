package ftb.lib.api;

import ftb.lib.FTBWorld;

public class EventFTBWorldClient extends EventLM
{
	public final FTBWorld world;
	public final boolean isFake;
	
	public EventFTBWorldClient(FTBWorld w, boolean b)
	{ world = w; isFake = b; }
}