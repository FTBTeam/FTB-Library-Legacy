package ftb.lib.api;

import com.google.common.collect.*;

public class GameModes
{
	public final ImmutableSortedSet<String> allModes;
	public final String defaultMode;
	public final String commonMode;
	public final ImmutableSortedMap<String, String> customData;
	
	public GameModes(ImmutableSortedSet<String> l, String d, String c, ImmutableSortedMap<String, String> cd)
	{ allModes = l; defaultMode = d; commonMode = c; customData = cd; }
}