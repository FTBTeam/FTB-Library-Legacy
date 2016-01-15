package ftb.lib.api.gui;

import ftb.lib.api.PlayerAction;

import java.util.*;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public class PlayerActionRegistry
{
	private static final HashMap<String, PlayerAction> map = new HashMap<>();
	
	public static void add(PlayerAction a)
	{ if(a != null) map.put(a.ID, a); }
	
	public static Collection<PlayerAction> getPlayerActions()
	{ return map.values(); }
}
