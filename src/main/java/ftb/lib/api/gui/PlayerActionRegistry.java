package ftb.lib.api.gui;

import ftb.lib.api.PlayerAction;
import ftb.lib.api.friends.ILMPlayer;
import latmod.lib.config.*;

import java.util.*;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public class PlayerActionRegistry
{
	private static final HashMap<String, PlayerAction> map = new HashMap<>();
	public static final Map<String, ConfigEntry> configEntries = new HashMap<>();
	
	public static final ConfigGroup configGroup = new ConfigGroup("sidebar_buttons")
	{
		public Map<String, ConfigEntry> entryMap()
		{ return configEntries; }
	};
	
	public static boolean enabled(String id)
	{
		ConfigEntry entry = configEntries.get(id);
		return (entry == null) ? true : entry.getAsBoolean();
	}
	
	public static void add(final PlayerAction a)
	{
		if(a != null)
		{
			map.put(a.getID(), a);
			
			if(a.configDefault() != null)
			{
				ConfigEntryBool entry = new ConfigEntryBool(a.getID(), a.configDefault().booleanValue())
				{
					public String getFullID()
					{ return "player_action." + a.getID(); }
				};
				
				configEntries.put(a.getID(), entry);
			}
		}
	}
	
	public static List<PlayerAction> getPlayerActions(PlayerAction.Type t, ILMPlayer self, ILMPlayer other, boolean sort)
	{
		ArrayList<PlayerAction> l = new ArrayList<>();
		
		for(PlayerAction a : map.values())
		{
			if(a.type.equalsType(t) && a.isVisibleFor(self, other))
			{
				if(a.configDefault() != null)
				{
					if(!enabled(a.getID())) continue;
				}
				
				l.add(a);
			}
		}
		
		if(sort) Collections.sort(l);
		return l;
	}
}
