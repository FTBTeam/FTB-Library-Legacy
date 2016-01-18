package ftb.lib.notification;

import latmod.lib.*;

import java.util.HashMap;

public class ClickActionRegistry
{
	private static final HashMap<String, ClickAction> map = new HashMap<>();
	
	static
	{
		add(ClickAction.CMD);
		add(ClickAction.SHOW_CMD);
		add(ClickAction.URL);
		add(ClickAction.FILE);
		add(ClickAction.GUI);
	}
	
	public static String[] getKeys()
	{ return LMListUtils.toStringArray(map.keySet()); }
	
	public static void add(ClickAction a)
	{
		if(a != null && LMStringUtils.isValid(a.ID) && !map.containsKey(a.ID)) map.put(a.ID, a);
	}
	
	public static ClickAction get(String s)
	{ return map.get(s); }
}