package ftb.lib.api.gui;

import java.util.HashMap;
import java.util.Map;

public class LMGuiHandlerRegistry
{
	private static final Map<String, LMGuiHandler> map = new HashMap<>();
	
	public static void add(LMGuiHandler h)
	{ if(h != null && !map.containsKey(h.ID)) map.put(h.ID, h); }
	
	public static LMGuiHandler get(String id)
	{ return map.get(id); }
}