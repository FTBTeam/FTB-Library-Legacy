package ftb.lib.api.gui;

import ftb.lib.api.*;
import ftb.lib.api.config.*;

import java.util.*;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public class PlayerActionRegistry
{
	private static final Map<String, PlayerAction> map = new HashMap<>();
	
	public static final ConfigGroup configGroup = new ConfigGroup("sidebar_buttons");
	
	public static boolean enabled(String id)
	{
		ConfigEntry entry = configGroup.entryMap.get(id);
		return (entry == null) || entry.getAsBoolean();
	}
	
	public static void add(final PlayerAction a)
	{
		if(a != null)
		{
			map.put(a.getID(), a);
			
			if(a.configDefault() != null)
			{
				ConfigEntryBool entry = new ConfigEntryBool(a.getID(), a.configDefault())
				{
					public String getFullID()
					{ return "player_action." + a.getID(); }
				};
				
				configGroup.entryMap.put(a.getID(), entry);
			}
		}
	}
	
	public static List<PlayerAction> getPlayerActions(PlayerAction.Type t, ForgePlayer self, ForgePlayer other, boolean sort, boolean ignoreConfig)
	{
		ArrayList<PlayerAction> l = new ArrayList<>();
		
		for(PlayerAction a : map.values())
		{
			if(a.type.equalsType(t) && a.isVisibleFor(self, other))
			{
				if(!ignoreConfig && a.configDefault() != null)
				{
					if(!enabled(a.getID())) continue;
				}
				
				l.add(a);
			}
		}
		
		if(sort) Collections.sort(l);
		return l;
	}
	
	public static PlayerAction get(String key)
	{ return map.get(key); }
}
