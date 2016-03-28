package ftb.lib.api.events;

import ftb.lib.api.ForgePlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public class ForgePlayerMPInfoEvent extends Event
{
	public final ForgePlayerMP player;
	public final List<IChatComponent> list;
	
	public ForgePlayerMPInfoEvent(ForgePlayerMP p, List<IChatComponent> l)
	{
		player = p;
		list = l;
	}
}
