package com.feed_the_beast.ftblib.events.universe;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import com.feed_the_beast.ftblib.lib.data.Universe;

/**
 * @author LatvianModder
 */
public abstract class UniverseEvent extends FTBLibEvent
{
	private final Universe universe;

	public UniverseEvent(Universe u)
	{
		universe = u;
	}

	public Universe getUniverse()
	{
		return universe;
	}
}