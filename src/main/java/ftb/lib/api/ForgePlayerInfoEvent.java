package ftb.lib.api;

import ftb.lib.api.players.LMPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public class ForgePlayerInfoEvent extends Event
{
	public final LMPlayer LMPlayer;
	public final List<IChatComponent> list;
	
	public ForgePlayerInfoEvent(LMPlayer p, List<IChatComponent> l)
	{
		LMPlayer = p;
		list = l;
	}
}
