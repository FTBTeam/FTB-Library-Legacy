package ftb.lib.api;

import ftb.lib.api.players.ForgeWorld;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventFTBReload extends Event
{
	public final ForgeWorld world;
	public final ICommandSender sender;
	public final boolean reloadingClient;
	
	public EventFTBReload(ForgeWorld w, ICommandSender ics, boolean b)
	{
		world = w;
		sender = ics;
		reloadingClient = b;
	}
}