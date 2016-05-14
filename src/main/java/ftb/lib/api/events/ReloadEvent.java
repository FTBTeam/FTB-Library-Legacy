package ftb.lib.api.events;

import ftb.lib.ReloadType;
import ftb.lib.api.ForgeWorld;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ReloadEvent extends Event
{
	public final ForgeWorld world;
	public final ICommandSender sender;
	public final ReloadType type;
	public final boolean login;
	
	public ReloadEvent(ForgeWorld w, ICommandSender ics, ReloadType t, boolean b)
	{
		world = w;
		sender = ics;
		type = t;
		login = b;
	}
}