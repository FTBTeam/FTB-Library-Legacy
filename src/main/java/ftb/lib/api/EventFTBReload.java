package ftb.lib.api;

import ftb.lib.FTBWorld;
import net.minecraft.command.ICommandSender;

public class EventFTBReload extends EventLM
{
	public final FTBWorld world;
	public final ICommandSender sender;
	public final boolean reloadingClient;
	
	public EventFTBReload(FTBWorld w, ICommandSender ics, boolean b)
	{
		world = w;
		sender = ics;
		reloadingClient = b;
	}
}