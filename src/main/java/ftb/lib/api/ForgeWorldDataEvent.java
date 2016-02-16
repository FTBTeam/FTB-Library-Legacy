package ftb.lib.api;

import ftb.lib.api.friends.*;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.*;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public class ForgeWorldDataEvent extends Event
{
	public final LMWorld LMWorld;
	private final Map<String, ForgeWorldData> map;
	
	public ForgeWorldDataEvent(LMWorld w)
	{
		LMWorld = w;
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
