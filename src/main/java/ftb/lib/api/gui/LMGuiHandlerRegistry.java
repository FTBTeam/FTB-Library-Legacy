package ftb.lib.api.gui;

import latmod.lib.FastMap;

public class LMGuiHandlerRegistry
{
	private static final FastMap<String, LMGuiHandler> map = new FastMap<String, LMGuiHandler>();
	
	public static void add(LMGuiHandler h)
	{ if(h != null && !map.containsKey(h.ID)) map.put(h.ID, h); }
	
	public static LMGuiHandler get(String id)
	{ return map.get(id); }
}