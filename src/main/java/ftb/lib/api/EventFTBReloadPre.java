package ftb.lib.api;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommandSender;

/** Should only be used by FTBUtilities! */
public class EventFTBReloadPre extends EventLM
{
	public final Side side;
	public final ICommandSender sender;
	
	public EventFTBReloadPre(Side s, ICommandSender ics)
	{ side = s; sender = ics; }
}