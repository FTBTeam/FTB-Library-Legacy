package ftb.lib.api.events;

import ftb.lib.api.ForgePlayer;
import ftb.lib.api.ForgePlayerData;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 10.02.2016.
 */
public class ForgePlayerDataEvent extends Event
{
	public final ForgePlayer player;
	private final Map<String, ForgePlayerData> map;
	
	public ForgePlayerDataEvent(ForgePlayer p)
	{
		player = p;
		map = new HashMap<>();
	}
	
	public void add(ForgePlayerData data)
	{
		if(!map.containsKey(data.getID()))
		{
			map.put(data.getID(), data);
		}
	}
	
	public Map<String, ForgePlayerData> getMap()
	{ return Collections.unmodifiableMap(map); }
}
