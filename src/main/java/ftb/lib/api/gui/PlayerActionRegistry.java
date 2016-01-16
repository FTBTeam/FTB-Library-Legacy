package ftb.lib.api.gui;

import ftb.lib.api.*;

import java.util.*;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public class PlayerActionRegistry
{
	private static final HashMap<String, PlayerAction> map = new HashMap<>();
	
	public static void add(PlayerAction a)
	{ if(a != null) map.put(a.ID, a); }
	
	public static List<PlayerAction> getPlayerActions(PlayerAction.Type t, ILMPlayer self, ILMPlayer other, boolean sort)
	{
		ArrayList<PlayerAction> l = new ArrayList<>();
		
		for(PlayerAction a : map.values())
		{
			if(a.type.equalsType(t) && a.isVisibleFor(self, other)) l.add(a);
		}
		
		if(sort) Collections.sort(l);
		return l;
	}
}
