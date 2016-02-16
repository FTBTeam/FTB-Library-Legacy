package ftb.lib.api;

import ftb.lib.api.friends.LMWorld;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventFTBReload extends Event
{
	public final LMWorld world;
	public final ICommandSender sender;
	public final boolean reloadingClient;
	
	public EventFTBReload(LMWorld w, ICommandSender ics, boolean b)
	{
		world = w;
		sender = ics;
		reloadingClient = b;
	}
}