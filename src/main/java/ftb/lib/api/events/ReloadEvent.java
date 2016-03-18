package ftb.lib.api.events;

import ftb.lib.api.ForgeWorld;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ReloadEvent extends Event
{
	public final ForgeWorld world;
	public final ICommandSender sender;
	public final boolean reloadingClient;
	public final boolean modeChanged;
	
	public ReloadEvent(ForgeWorld w, ICommandSender ics, boolean b, boolean b1)
	{
		world = w;
		sender = ics;
		reloadingClient = b;
		modeChanged = b1;
	}
}