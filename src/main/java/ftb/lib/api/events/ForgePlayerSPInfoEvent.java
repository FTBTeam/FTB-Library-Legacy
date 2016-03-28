package ftb.lib.api.events;

import ftb.lib.api.ForgePlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

/**
 * Created by LatvianModder on 10.02.2016.
 */
@SideOnly(Side.CLIENT)
public class ForgePlayerSPInfoEvent extends Event
{
	public final ForgePlayerSP player;
	public final List<String> list;
	
	public ForgePlayerSPInfoEvent(ForgePlayerSP p, List<String> l)
	{
		player = p;
		list = l;
	}
}
