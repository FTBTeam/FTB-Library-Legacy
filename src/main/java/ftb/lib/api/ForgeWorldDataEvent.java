package ftb.lib.api;

import ftb.lib.api.players.*;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.*;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public class ForgeWorldDataEvent extends Event
{
	public final LMWorld world;
	private final Map<String, ForgeWorldData> map;
	
	public ForgeWorldDataEvent(LMWorld w)
	{
		world = w;
		map = new HashMap<>();
	}
	
	public void add(ForgeWorldData data)
	{
		if(!map.containsKey(data.ID))
		{
			map.put(data.ID, data);
		}
	}
	
	public Map<String, ForgeWorldData> getMap()
	{ return Collections.unmodifiableMap(map); }
}
