package ftb.lib.api;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommandSender;

public class EventFTBReload extends EventLM
{
	public final Side side;
	public final ICommandSender sender;
	
	public EventFTBReload(Side s, ICommandSender ics)
	{ side = s; sender = ics; }
}