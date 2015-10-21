package ftb.lib.api;

import java.util.*;

public class GameModes
{
	public final List<String> allModes;
	public final String defaultMode;
	public final String commonMode;
	public final Map<String, String> customData;
	
	public GameModes(List<String> l, String d, String c, Map<String, String> cd)
	{
		if(l == null || l.isEmpty()) throw new RuntimeException("gamemodes.json|mods can't be empty!");
		allModes = l;
		defaultMode = d;
		commonMode = c;
		customData = cd;
	}
}