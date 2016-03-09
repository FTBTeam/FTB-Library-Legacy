package ftb.lib.api.events;

import ftb.lib.api.ForgeWorld;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ReloadEvent extends Event
{
	public final ForgeWorld world;
	public final ICommandSender sender;
	public final boolean reloadingClient;
	
	public ReloadEvent(ForgeWorld w, ICommandSender ics, boolean b)
	{
		world = w;
		sender = ics;
		reloadingClient = b;
	}
}