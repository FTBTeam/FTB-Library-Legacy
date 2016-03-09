package ftb.lib.api.events;

import ftb.lib.api.ForgePlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public class ForgePlayerInfoEvent extends Event
{
	public final ftb.lib.api.ForgePlayer ForgePlayer;
	public final List<IChatComponent> list;
	
	public ForgePlayerInfoEvent(ForgePlayer p, List<IChatComponent> l)
	{
		ForgePlayer = p;
		list = l;
	}
}
