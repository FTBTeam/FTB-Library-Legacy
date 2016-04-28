package ftb.lib.api.events;

import ftb.lib.api.ForgeWorld;
import ftb.lib.api.ForgeWorldData;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public class ForgeWorldDataEvent extends Event
{
	public final ForgeWorld world;
	private final Map<String, ForgeWorldData> map;
	
	public ForgeWorldDataEvent(ForgeWorld w)
	{
		world = w;
		map = new HashMap<>();
	}
	
	public void add(ForgeWorldData data)
	{
		if(!map.containsKey(data.getID()))
		{
			map.put(data.getID(), data);
		}
	}
	
	public Map<String, ForgeWorldData> getMap()
	{ return Collections.unmodifiableMap(map); }
}
