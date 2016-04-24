package ftb.lib.api;

import ftb.lib.FTBWorld;
import ftb.lib.ReloadType;
import net.minecraft.command.ICommandSender;

public class EventFTBReload extends EventLM
{
	public final FTBWorld world;
	public final ICommandSender sender;
	public final ReloadType type;
	public final boolean login;
	
	public EventFTBReload(FTBWorld w, ICommandSender ics, ReloadType t, boolean b)
	{
		world = w;
		sender = ics;
		type = t;
		login = b;
	}
}