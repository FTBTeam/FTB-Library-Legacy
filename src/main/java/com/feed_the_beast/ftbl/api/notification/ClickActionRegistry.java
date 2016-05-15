package com.feed_the_beast.ftbl.api.notification;

import latmod.lib.LMListUtils;
import latmod.lib.LMStringUtils;

import java.util.HashMap;

public class ClickActionRegistry
{
	private static final HashMap<String, ClickActionType> map = new HashMap<>();
	
	static
	{
		add(ClickActionType.ACTION);
		add(ClickActionType.CMD);
		add(ClickActionType.SHOW_CMD);
		add(ClickActionType.URL);
		add(ClickActionType.FILE);
		add(ClickActionType.GUI);
		add(ClickActionType.FRIEND_ADD);
		add(ClickActionType.FRIEND_ADD_ALL);
	}
	
	public static String[] getKeys()
	{ return LMListUtils.toStringArray(map.keySet()); }
	
	public static void add(ClickActionType a)
	{
		if(a != null && LMStringUtils.isValid(a.getID()) && !map.containsKey(a.getID())) { map.put(a.getID(), a); }
	}
	
	public static ClickActionType get(String s)
	{ return map.get(s); }
}