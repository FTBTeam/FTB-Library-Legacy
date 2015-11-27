package ftb.lib.api;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommandSender;

public class EventFTBReload extends EventLM
{
	public final Side side;
	public final ICommandSender sender;
	public final boolean reloadingClient;
	
	public EventFTBReload(Side s, ICommandSender ics, boolean b)
	{ side = s; sender = ics; reloadingClient = b; }
}